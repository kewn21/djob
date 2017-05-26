/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.impl;

import java.util.List;

import org.kesy.djob.core.utils.DateUtils;
import org.kesy.djob.sdu.api.JobManager;
import org.kesy.djob.sdu.api.JobSchedulerFactory;
import org.kesy.djob.sdu.api.JobStoreFactory;
import org.kesy.djob.sdu.api.consts.JobStatus;
import org.kesy.djob.sdu.api.exception.JobException;
import org.kesy.djob.sdu.api.exception.JobRunTimeException;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage;
import org.kesy.djob.sdu.api.task.TaskRunParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类<code>{@link JobManagerImpl}</code> 创建于 Jan 9, 2014<br/>
 * 类描述:{@link JobManager}的默认实现
 * <p>
 * 
 * @author kewn
 */
public class JobManagerImpl implements JobManager {

	private static final Logger logger = LoggerFactory.getLogger(JobManagerImpl.class);
	
	/**
	 * 功能描述:
	 * 
	 * @param jobInfo
	 * @param exectMode
	 * @return
	 * @author kewn
	 */
	protected boolean checkJobInfoMode(JobInfo jobInfo, String exectMode) {
		return exectMode.equalsIgnoreCase(jobInfo.getStartMode());
	}

	/**
	 * 功能描述:
	 * 
	 * @return
	 * @author kewn
	 */
	private String[] selectAll4StrArra() {
		List<JobInfo> jobInfos = JobStoreFactory.get().selectJobs();
		String[] jobIds = new String[jobInfos.size()];
		int index = 0;
		for (JobInfo jobInfo : jobInfos) {
			jobIds[index] = jobInfo.getId();
			index++;
		}
		return jobIds;
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see
	 * org.kesy.djob.sdu.JobManager#createJob(org.kesy.djob.sdu
	 * .core.model.JobMessageModels.JobMessage)
	 * 
	 * @author kewn
	 */
	@Override
	public boolean createJob(JobMessage jobMessage) {
		if (jobMessage == null) {
			throw new NullPointerException("JobMessage must be not null.");
		}
		
		JobInfo jobInfo = PackageJobInfo.packageJobInfo(jobMessage);
		jobInfo = JobStoreFactory.get().insertJob(jobInfo);
		if (jobInfo != null && checkJobInfoMode(jobInfo, JobStatus.JOB_MODE_AUTO)) {
			try {
				return JobSchedulerFactory.get().createJob(jobInfo);
			} catch (JobException e) {
				logger.error("createJob error " + e.getMessage(), e);
				throw new JobRunTimeException("createJob error " + e.getMessage(), e);
			}
		}
		
		return false;
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#dropAll()
	 * 
	 * @author kewn
	 */
	@Override
	public boolean dropAll() {
		String[] jobIds = selectAll4StrArra();
		return dropJobs(jobIds);
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#dropJob(java.lang.String)
	 * 
	 * @author kewn
	 */
	@Override
	public boolean dropJob(String jobId) {
		if (!JobSchedulerFactory.get().dropJob(jobId)) {
			return false;
		}
		
		String[] jobIds = new String[] { jobId };
		return JobStoreFactory.get().deleteJobs(jobIds);
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#dropJobs(java.lang.String[])
	 * 
	 * @author kewn
	 */
	@Override
	public boolean dropJobs(String[] jobIds) {
		if (jobIds == null || jobIds.length == 0) {
			return true;
		}
		
		boolean result = true;
		for (String jobId : jobIds) {
			if (!dropJob(jobId)) {
				result = false;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#findJob(java.lang.String)
	 * 
	 * @author kewn
	 */
	@Override
	public JobInfo findJob(String jobId) {
		JobInfo jobInfo = JobStoreFactory.get().selectJob(jobId);
		if (jobInfo != null) {
			// 是否需要设置taskinfo，是否设置taskinfo，依据JobStoreFactory.get()实现
			// jobinfo的taskinfo属性不合理应该为list
			// JobInfo.Builder builder =
			// JobInfo.newBuilder().mergeFrom(jobInfo);
			// JobStoreFactory.get().
			// builder.setTasks(value)
		}
		return jobInfo;
	}

	@Override
	public List<JobInfo> listJobs() {
		List<JobInfo> jobInfos = JobStoreFactory.get().selectJobs();
		if (jobInfos != null && jobInfos.size() != 0) {
			// 是否设置taskinfo，依据JobStoreFactory.get()实现
		}
		return jobInfos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.sdu.JobManager#restartAll()
	 */
	@Override
	public boolean restartAll() {
		String[] jobIds = selectAll4StrArra();
		return restartJobs(jobIds);
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#restartJob(java.lang.String)
	 * 
	 * @author kewn
	 */
	@Override
	public boolean restartJob(String jobId) {
		JobInfo jobInfo = JobStoreFactory.get().selectJob(jobId);
		if (jobInfo != null && !checkJobInfoMode(jobInfo, JobStatus.JOB_MODE_FORBIDDEN)) {
			return JobSchedulerFactory.get().restartJob(jobId);
		}
		return false;
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see
	 * org.kesy.djob.sdu.JobManager#restartJobs(java.lang.String[])
	 * 
	 * @author kewn
	 */
	@Override
	public boolean restartJobs(String[] jobIds) {
		if (jobIds == null || jobIds.length == 0) {
			return true;
		}
		
		boolean result = true;
		for (String jobId : jobIds) {
			if (!restartJob(jobId)) {
				result = false;// 可以捕获某个异常的job
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#runJob(java.lang.String)
	 * 
	 * @author kewn
	 */
	@Override
	public boolean runJob(String jobId, TaskRunParam param) {
		wrapRunParam(param);
		
		JobInfo jobInfo = JobStoreFactory.get().selectJob(jobId);
		if (jobInfo != null && !checkJobInfoMode(jobInfo, JobStatus.JOB_MODE_FORBIDDEN)) {
			logger.info("runJob jobId[" + jobId + "]");
			return JobSchedulerFactory.get().runJob(jobId, param);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.JobManager#runJobs(java.lang.String[], org.kesy.djob.sdu.api.task.TaskRunParam)
	 */
	@Override
	public boolean runJobs(String[] jobIds, TaskRunParam param) {
		if (jobIds == null || jobIds.length == 0) {
			throw new IllegalArgumentException("Select at least one work.");
		}
		
		wrapRunParam(param);
		
		boolean result = true;
		for (String jobId : jobIds) {
			if (!runJob(jobId, param)){
				result = false;
			}
		}
		return result;
	}
	
	private void wrapRunParam(TaskRunParam param){
		if (param.getDataFrom() <= 0) {
			param.setDataFrom(DateUtils.today());
		}
		
		if (param.getDataTo() <= 0) {
			param.setDataTo(DateUtils.today());
		}
		
		if (param.getInterval() <= 0) {
			param.setInterval(1);
		}
		
		if (param.getDataFrom() > param.getDataTo()) {
			throw new IllegalArgumentException("DataFrom can't more than DataTo");
		}
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.sdu.JobManager#shundownAll()
	 */
	@Override
	public boolean shutdownAll() {
		String[] jobIds = selectAll4StrArra();
		return shutdownJobs(jobIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.sdu.JobManager#shundownJob(java.lang.String)
	 */
	@Override
	public boolean shutdownJob(String jobId) {
		if (!JobSchedulerFactory.get().shutdownJob(jobId)) {
			return false;
		}
		return JobStoreFactory.get()
				.updateJobStatus(jobId, JobStatus.JOB_STATUS_SHUTDOWN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.sdu.JobManager#shundownJobs(java.lang.String[])
	 */
	@Override
	public boolean shutdownJobs(String[] jobIds) {
		boolean result = true;
		for (String jobId : jobIds) {
			if (!shutdownJob(jobId)) {
				result = false;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#startAll()
	 * 
	 * @author kewn
	 */
	@Override
	public boolean startAll() {
		List<JobInfo> jobInfos = JobStoreFactory.get().selectJobs();

		boolean result = true;
		/////////////////////////start ALL 特别规则，默认startALL是被重启的时候调用的，/////////////////////////////////////////
		///////////如果系统在允许过程重新调用start ALL会有问题，除非能区分从哪个入口进行调用
		for (JobInfo job : jobInfos) {
			try {
				String status = null;
				if (JobStatus.JOB_STATUS_WAITING.equals(job.getRunStatus()) 
						|| JobStatus.JOB_STATUS_RUNNING.equals(job.getRunStatus())) {
					status = JobStatus.JOB_STATUS_INTERRUPT;

					if (!JobStoreFactory.get().updateJobStatus(job.getId(), status)) {
						 logger.error(String.format("update job [%s] run status failed when starting all jobs", job.getId()));
						continue;
					} 
					
					if (!JobStoreFactory.get().updateRunningTaskStatusToInterrupt(job.getId())) {
						logger.error(String.format("update task [%s] run status failed when starting all jobs", job.getId()));
						continue;
					}
				}
				else {
					if (!JobStatus.JOB_MODE_AUTO.equals(job.getStartMode())) {
						status = JobStatus.JOB_STATUS_SHUTDOWN;
					} else {
						status = JobStatus.JOB_STATUS_STANDBY;
					}
				}
				
				///////////////////////重置状态，重要
				JobInfo.Builder builder = JobInfo.newBuilder(job);
				builder.setRunStatus(status);
				JobInfo jobInfo = builder.build();
				
				////////////////////////
				//应该是auto才启动
				if (checkJobInfoMode(jobInfo, JobStatus.JOB_MODE_AUTO)) {
					if (!JobSchedulerFactory.get().createJob(jobInfo)) {
						result = false;
					}
				}
			} catch (Exception e) {
				result = false;
				logger.error("startJob " + job.getId() + " error " + e.getMessage(), e);
			}
		}
		
		return result;
		///////////////////////////////////////////////////////////////////
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#startJob(java.lang.String)
	 * 
	 * @author kewn
	 */
	@Override
	public boolean startJob(String jobId) {
		logger.info(String.format("start to start job [%s]...", jobId));
		
		JobInfo jobInfo = JobStoreFactory.get().selectJob(jobId);
		if (jobInfo == null) {
			logger.info(String.format("job [%s] info is null", jobId));
			return false;
		}
		
		logger.info("job info : " + jobInfo.toString());
		
		if (!checkJobInfoMode(jobInfo, JobStatus.JOB_MODE_FORBIDDEN)) {
			try {
				logger.info(String.format("start job [%s] on scheduler...",  jobId));
				
				if (!JobSchedulerFactory.get().jobIsExist(jobId)) {
					if (!JobSchedulerFactory.get().createJob(jobInfo)) {
						return false;
					}
				}
				
				return JobStoreFactory.get().updateJobStatus(jobId, JobStatus.JOB_STATUS_STANDBY);
			} catch (JobException e) {
				logger.error("startJob error " + e.getMessage(), e);
				throw new JobRunTimeException("startJob error " + e.getMessage(), e);
			}
		}
		else {
			logger.info(String.format("job [%s] is forbiddenned", jobId));
		}
		
		return true;
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#startJobs(java.lang.String[])
	 * 
	 * @author kewn
	 */
	@Override
	public boolean startJobs(String[] jobIds) {
		if (jobIds == null || jobIds.length == 0) {
			return true;
		}
		
		boolean result = true;
		for (String jobId : jobIds) {
			if (!startJob(jobId)) {
				result = false;// 可以捕获某个异常的job
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#stopAll()
	 * 
	 * @author kewn
	 */
	@Override
	public boolean stopAll() {
		// jobOperater获取回原生的scheduler调用stop，可能有好的效率
		String[] jobIds = selectAll4StrArra();
		return stopJobs(jobIds);
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#stopJob(java.lang.String)
	 * 
	 * @author kewn
	 */
	@Override
	public boolean stopJob(String jobId) {
		return JobSchedulerFactory.get().stopJob(jobId);
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see org.kesy.djob.sdu.JobManager#stopJobs(java.lang.String[])
	 * 
	 * @author kewn
	 */
	@Override
	public boolean stopJobs(String[] jobIds) {
		boolean result = true;
		for (String jobId : jobIds) {
			if (!JobSchedulerFactory.get().stopJob(jobId)) {
				result = false;
				// 某个job调用停止失败，如异常已经处理，可能会继续调度停止其他的job
				// 可以捕获处理失败的job
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc) 功能描述:
	 * 
	 * @see
	 * org.kesy.djob.sdu.JobManager#updateJob(org.kesy.djob.sdu
	 * .core.model.JobMessageModels.JobMessage)
	 * 
	 * @author kewn
	 */
	@Override
	public boolean updateJob(JobMessage jobMessage) {
		if (jobMessage == null) {
			throw new NullPointerException("JobMessage must be not null.");
		}
		
		logger.info(String.format("start to update job [%s]...", jobMessage.getName()));
		JobInfo jobInfo = JobStoreFactory.get().selectJob(jobMessage.getId());
		
		logger.info("job info : " + jobInfo.toString());
		jobInfo = PackageJobInfo.updateJobInfo(jobMessage, jobInfo);
		
		logger.info(String.format("start to update job [%s] store...",  jobMessage.getName()));
		if (!JobStoreFactory.get().updateJob(jobInfo)) {
			return false;
		}
		
		logger.info(String.format("success in update job [%s] store...",  jobMessage.getName()));
		if (!JobSchedulerFactory.get().updateJob(jobInfo)) {
			logger.info(String.format("fail in update job [%s] store...",  jobMessage.getName()));
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobManager#jobIsExist(java.lang.String)
	 */
	@Override
	public boolean jobIsExist(String jobId) {
		return JobStoreFactory.get().selectJob(jobId) != null;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobManager#updateJobStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateJobStatus(String jobId, String status) {
		return JobStoreFactory.get().updateJobStatus(jobId, status);
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobManager#updateTaskStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateTaskStatus(String taskId, String status) {
		return JobStoreFactory.get().updateTaskStatus(taskId, status);
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobManager#updateJobStatusWhenUnExecutting(java.lang.String)
	 */
	@Override
	public boolean updateJobStatusWhenNotRunning(String jobId) {
		return JobStoreFactory.get().updateJobStatusToWaiting(jobId);
	}
}
