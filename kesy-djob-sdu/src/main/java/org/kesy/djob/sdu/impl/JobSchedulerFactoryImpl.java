/**
 * This file created at 2014-3-13.
 *
 */
package org.kesy.djob.sdu.impl;

import org.apache.commons.lang.StringUtils;
import org.kesy.djob.sdu.api.JobScheduler;
import org.kesy.djob.sdu.api.JobSchedulerFactory;
import org.kesy.djob.sdu.api.consts.JobStatus;
import org.kesy.djob.sdu.api.exception.JobException;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.task.TaskRunParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类<code>{@link JobSchedulerFactoryImpl}</code>  创建于 2014-3-13<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class JobSchedulerFactoryImpl extends JobSchedulerFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(JobSchedulerFactoryImpl.class);
	
	private JobScheduler timingScheduler;
	public void setTimingScheduler(JobScheduler timingScheduler) {
		this.timingScheduler = timingScheduler;
	}
	
	private JobScheduler runOnceScheduler;
	public void setRunOnceScheduler(JobScheduler runOnceScheduler) {
		this.runOnceScheduler = runOnceScheduler;
	}


	/**
	 * 功能描述:创建job</p>
	 * <li>1、解释jobInfo执行规则</li>
	 * <li>2、构建job</li>
	 * <li>3、启动job，放置等待执行队列</li>
	 * @param jobInfo
	 * @author kewn
	 * @throws JobException 
	 */
	public boolean createJob(JobInfo jobInfo) throws JobException{
		if (StringUtils.isNotEmpty(jobInfo.getExecStrategy())) {
			if (!timingScheduler.jobIsExist(jobInfo.getId())) {
				return timingScheduler.createJob(jobInfo);
			}
			return false;
		}
		else {
			return runOnceScheduler.createJob(jobInfo);
		}
	}

	/**
	 * 功能描述:删除job
	 * <li></li>
	 * @param jobId
	 * @return
	 * @author kewn
	 */
	public boolean dropJob(String jobId){
		return timingScheduler.dropJob(jobId);
	}

	public boolean restartJob(String jobId){
		return timingScheduler.restartJob(jobId);
	}

	public boolean runJob(String jobId, TaskRunParam param) {
		if (timingScheduler.jobIsExist(jobId)) {
			return timingScheduler.runJob(jobId, param);
		}
		else {
			return runOnceScheduler.runJob(jobId, param);
		}
	}

	public boolean shutdownJob(String jobId){
		return timingScheduler.shundownJob(jobId);
	}

	public boolean stopJob(String jobId){
		return timingScheduler.stopJob(jobId);
	}

	public boolean updateJob(JobInfo jobInfo){
		if (StringUtils.isNotEmpty(jobInfo.getExecStrategy())) {
			if (runOnceScheduler.jobIsExist(jobInfo.getId())) {
				runOnceScheduler.dropJob(jobInfo.getId());
			}
			
			if (timingScheduler.jobIsExist(jobInfo.getId())) {
				if (JobStatus.JOB_MODE_AUTO.equals(jobInfo.getStartMode())) {
					return timingScheduler.updateJob(jobInfo);
				}
				else {
					return timingScheduler.dropJob(jobInfo.getId());
				}
			}
			else {
				try {
					if (JobStatus.JOB_MODE_AUTO.equals(jobInfo.getStartMode())) {
						return timingScheduler.createJob(jobInfo);
					}
				} catch (JobException e) {
					logger.error("create job error : " + e);
					return false;
				}
			}
		}
		else {
			if (timingScheduler.jobIsExist(jobInfo.getId())) {
				timingScheduler.dropJob(jobInfo.getId());
			}
		}

		return true;
	}
	
	public boolean jobIsExist(String jobId){
		return timingScheduler.jobIsExist(jobId);
	}
}
