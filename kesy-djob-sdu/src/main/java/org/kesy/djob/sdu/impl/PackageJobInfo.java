/**
 * This file created at Jan 9, 2014.
 *
 */
package org.kesy.djob.sdu.impl;

import org.apache.commons.lang.StringUtils;
import org.kesy.djob.sdu.api.consts.JobStatus;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage.TaskMessage;

/**
 * 类<code>{@link PackageJobInfo}</code> 创建于 Jan 9, 2014<br/>
 * 类描述:
 * <p>
 * <li></li>
 * 
 * @author kewn
 */
public class PackageJobInfo {

	/**
	 * 功能描述:
	 * 
	 * @param jobMessage
	 * @return
	 * @author kewn
	 */
	public static JobInfo packageJobInfo(JobMessage jobMessage) {
		JobInfo.Builder builder = JobInfo.newBuilder();
		
		builder.setId(jobMessage.getId());
		builder.setName(jobMessage.getName());
		//update by kewn 20140214
		//启动模式为auto的，状态设定为stanby，其他的为shutdown
		if (JobStatus.JOB_MODE_AUTO.equals(jobMessage.getStartMode())) {
			if (StringUtils.isNotEmpty(jobMessage.getExecStrategy())) {
				builder.setRunStatus(JobStatus.JOB_STATUS_STANDBY);
			}
			else {
				builder.setRunStatus(JobStatus.JOB_STATUS_WAITING);
			}
		} else {
			builder.setRunStatus(JobStatus.JOB_STATUS_SHUTDOWN);
		}
		builder.setStartMode(jobMessage.getStartMode());
		builder.setDescription(jobMessage.getDescription());
		builder.setExecStrategy(jobMessage.getExecStrategy());
		builder.setExecName(jobMessage.getTaskMessage().getExecName());
		builder.setExecParam(jobMessage.getTaskMessage().getExecParam());
		builder.setPrincipal(jobMessage.getPrincipal());
		builder.setCreator(jobMessage.getCreator());
		
		return builder.build();
	}

	/**
	 * 功能描述:
	 * @param jobMessage
	 * @param jobInfo
	 * @return
	 * @author kewn
	 */
	public static JobInfo updateJobInfo(JobMessage jobMessage, JobInfo jobInfo) {
		JobInfo.Builder builder = JobInfo.newBuilder(jobInfo);
		
		if (jobMessage.hasName()) {
			builder.setName(jobMessage.getName());
		}
		if (jobMessage.hasDescription()) {
			builder.setDescription(jobMessage.getDescription());
		}
		if (jobMessage.hasStartMode()) {
			builder.setStartMode(jobMessage.getStartMode());
		}
		if (jobMessage.hasExecStrategy()) {
			builder.setExecStrategy(jobMessage.getExecStrategy());
		}

		if (jobMessage.hasStartMode()) {
			builder.setStartMode(jobMessage.getStartMode());
			
			if (!JobStatus.JOB_STATUS_RUNNING.equals(jobInfo.getRunStatus())
				&& !JobStatus.JOB_STATUS_WAITING.equals(jobInfo.getRunStatus())) {
				
				if (JobStatus.JOB_MODE_AUTO.equals(jobMessage.getStartMode())) {
					builder.setRunStatus(JobStatus.JOB_STATUS_STANDBY);
				} else {
					builder.setRunStatus(JobStatus.JOB_STATUS_SHUTDOWN);
				}
				
			}
		}

		if (jobMessage.hasTaskMessage()) {
			TaskMessage taskMessage = jobMessage.getTaskMessage();
			if (taskMessage.hasExecName()) {
				builder.setExecName(taskMessage.getExecName());
			}

			if (taskMessage.hasExecParam()) {
				builder.setExecParam(taskMessage.getExecParam());
			}
		}
		
		return builder.build();
	}

}
