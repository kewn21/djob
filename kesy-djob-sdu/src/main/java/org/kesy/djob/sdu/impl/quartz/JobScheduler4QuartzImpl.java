/**
 * This file created at Jan 7, 2014.
 *
 */
package org.kesy.djob.sdu.impl.quartz;

import java.util.List;

import org.kesy.djob.sdu.api.JobScheduler;
import org.kesy.djob.sdu.api.JobStoreFactory;
import org.kesy.djob.sdu.api.JobWrapper;
import org.kesy.djob.sdu.api.JobWrapperBuilder;
import org.kesy.djob.sdu.api.consts.JobStatus;
import org.kesy.djob.sdu.api.exception.JobException;
import org.kesy.djob.sdu.api.exception.JobRunTimeException;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.task.TaskRunParam;
import org.kesy.djob.sdu.impl.quartz.job.AllowConcurrentJobQuartz;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类<code>{@link JobScheduler4QuartzImpl}</code> 创建于 Jan 7, 2014<br/>
 * 类描述:jobOPerator的Quartz版本实现
 * <p>
 * <li></li>
 * 
 * @author kewn
 */
public class JobScheduler4QuartzImpl implements JobScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(JobScheduler4QuartzImpl.class);

	protected Scheduler scheduler;

	public JobScheduler4QuartzImpl() {
		createScheduler();
	}
	
	protected void createScheduler() {
		try {
			scheduler = QuartzSchedulerFactory.getNewScheduler();
			
			if (!scheduler.isStarted()) {
				logger.info("JobScheduler4QuartzImpl scheduler start");
				scheduler.start();
			}
		} catch (SchedulerException e) {
			logger.error("JobScheduler4QuartzImpl init error : " + e.getMessage(), e);
			throw new JobRunTimeException("JobScheduler4QuartzImpl init error : " + e.getMessage(), e);
		}
	}

	protected boolean checkScheduler() {
		return true;
	}

	protected boolean checkJobInfoStatus(String jobId, String expectStatus) {
		JobInfo jobInfo = JobStoreFactory.get().selectJob(jobId);
		return checkJobInfoStatus(jobInfo, expectStatus);
	}

	protected boolean checkJobInfoStatus(JobInfo jobInfo, String expectStatus) {
		return expectStatus.equalsIgnoreCase(jobInfo.getRunStatus()); // 是否需要考虑quartz的状态
	}

	@Override
	public boolean createJob(JobInfo jobInfo) throws JobException {
		/*if (checkJobInfoStatus(jobInfo, JobStatus.JOB_STATUS_RUNNING)) {
			throw new IllegalArgumentException("Wrong Job Status!");
		}*/
		
		String jobId = jobInfo.getId();
		String cronExpression = jobInfo.getExecStrategy();
		// 1、解释执行规则
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(
				CronScheduleBuilder.cronSchedule(cronExpression))
				.build();
		// 2、构建JobWrapper
		JobWrapper jobWrapper = JobWrapperBuilder.newBuilder(
				jobInfo.getExecName(), jobId, jobInfo.getExecParam())
				.build();
		jobWrapper.init();
		// 3、构建job
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("jobWrapper", jobWrapper);
		JobParam jobParam = new JobParam();//用于job的动态传参，quartz的datamap应该是浅度拷贝
		jobDataMap.put("jobParam", jobParam);
		JobBuilder jobBuilder = JobBuilder.newJob(AllowConcurrentJobQuartz.class)
				.withIdentity(jobId).usingJobData(jobDataMap);
		JobDetail jobDetail = jobBuilder.build();
		try {
			logger.info("createJob ->\n\t{}", jobDetail);
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			logger.error("createJob error " + jobId + " " + e.getMessage(), e);
			throw new JobException("createJob error " + jobId + " " + e.getMessage(), e);
		}
		
		return true;
	}

	@Override
	public boolean dropJob(String jobId) {
		/*if (checkJobInfoStatus(jobId, JobStatus.JOB_STATUS_RUNNING)) {
			throw new IllegalArgumentException("can't delete a running job");
		}*/
		
		JobKey jobKey = new JobKey(jobId);
		try {
			logger.info("scheduler deleteJob [" + jobKey + "]");
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			logger.error("dropJob error " + jobId + " " + e.getMessage(), e);
			throw new JobRunTimeException("dropJob error " + jobId + " " + e.getMessage(), e);
		}
		return true;
	}

	@Override
	public boolean restartJob(String jobId) {
		JobKey jobKey = new JobKey(jobId);
		JobInfo jobInfo = JobStoreFactory.get().selectJob(jobId);
		try {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			if (jobDetail != null) {
				if (checkJobInfoStatus(jobInfo, JobStatus.JOB_STATUS_RUNNING)) {
					shundownJob(jobId);
					createJob(jobInfo);
				} else {
					List<? extends Trigger> jobTrigger = scheduler.getTriggersOfJob(jobKey);
					scheduler.scheduleJob(jobTrigger.get(0));
				}
			} else {
				createJob(jobInfo);
			}
		} catch (Exception e) {
			logger.error("restartJob error " + e.getMessage(), e);
			throw new JobRunTimeException("restartJob error " + e.getMessage(), e);
		}

		return true;
	}

	@Override
	public boolean runJob(final String jobId, final TaskRunParam param) {
		JobKey jobKey = new JobKey(jobId);
		JobDetail jobDetail;
		try {
			jobDetail = scheduler.getJobDetail(jobKey);
			if (jobDetail == null) {
				throw new NullPointerException("jobId [" + jobId + "] not found in current scheduler");
			}
			JobParam jobParam = (JobParam)scheduler.getJobDetail(jobKey).getJobDataMap().get("jobParam");
			jobParam.setJobId(jobId);
			jobParam.setParam(param);
			scheduler.triggerJob(jobKey);// 马上触发一次job运行
		} catch (Exception e) {
			logger.error("runJob error " + e.getMessage(), e);
			throw new JobRunTimeException("runJob error " + e.getMessage(), e);
		}
		
		return true;
	}

	@Override
	public boolean shundownJob(String jobId) {
		try {
			logger.info("shundownJob job [" + jobId + "]");
			JobKey jobKey = new JobKey(jobId);
			scheduler.deleteJob(jobKey);
			//删除后，检查下是否已经删除
			logger.info("scheduler.checkExists [" + jobKey + "] [" + scheduler.checkExists(jobKey) + "]");
		} catch (Exception e) {
			logger.error("shundownJob error " + e.getMessage(), e);
			throw new JobRunTimeException("shundownJob error " + e.getMessage(), e);
		}
		
		return true;
	}

	@Override
	public boolean stopJob(String jobId) {
		logger.info("stopJob [" + jobId + "]");
		try {
			if (checkJobInfoStatus(jobId, JobStatus.JOB_STATUS_RUNNING)) {
				JobKey jobKey = new JobKey(jobId);
				logger.info("scheduler#interrupt [" + jobId + "], unscheduleJob trigger key [" + jobKey + "]");
				scheduler.interrupt(jobKey);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new JobRunTimeException("stopJob error " + e.getMessage(), e);
		}
		
		return true;
	}

	@Override
	public boolean updateJob(JobInfo jobInfo) {
		String jobId = jobInfo.getId();
		try {
			// 1、解释执行规则
			String cronExpression = jobInfo.getExecStrategy();
			Trigger newTrigger = TriggerBuilder.newTrigger().withSchedule(
					CronScheduleBuilder.cronSchedule(cronExpression))
					.build();
			// 2、构建JobWrapper
			JobWrapper jobWrapper = JobWrapperBuilder.newBuilder(
					jobInfo.getExecName(), jobId, jobInfo.getExecParam())
					.build();
			jobWrapper.init();
			
			//update by kewn 20140215，需要加上jobParam
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("jobWrapper", jobWrapper);
			JobParam jobParam = new JobParam();//用于job的动态传参，quartz的datamap应该是浅度拷贝
			jobDataMap.put("jobParam", jobParam);
			JobBuilder jobBuilder = JobBuilder.newJob(AllowConcurrentJobQuartz.class)
					.withIdentity(jobId).usingJobData(jobDataMap);
			jobBuilder.withIdentity(jobId);
			JobDetail jobDetail = jobBuilder.build();
			// 2、更新job信息
			scheduler.addJob(jobDetail, true, true);
			// 3、更新trigger查询
			List<? extends Trigger> triggerKeys = scheduler.getTriggersOfJob(jobDetail.getKey());
			scheduler.rescheduleJob(triggerKeys.get(0).getKey(), newTrigger);
		} catch (Exception e) {
			logger.error("updateJob error " + jobId + " " + e.getMessage(), e);
			throw new JobRunTimeException("updateJob error " + jobId + " " + e.getMessage(), e);
		}
		
		return true;
	}


	@Override
	public boolean jobIsExist(String jobId) {
		JobKey jobKey = new JobKey( jobId);
		try {
			return scheduler.checkExists(jobKey);
		} catch (SchedulerException e) {
			logger.error("jobIsExist error " + e.getMessage(), e);
			throw new JobRunTimeException("jobIsExist error " + e.getMessage(), e);
		}
	}
}
