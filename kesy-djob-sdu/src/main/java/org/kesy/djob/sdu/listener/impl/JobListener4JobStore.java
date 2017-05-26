/**
 * This file created at Jan 13, 2014.
 *
 */
package org.kesy.djob.sdu.listener.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kesy.djob.sdu.api.JobListener;
import org.kesy.djob.sdu.api.JobStoreFactory;
import org.kesy.djob.sdu.api.cluster.ClusterInfo;
import org.kesy.djob.sdu.api.consts.JobStatus;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfo;
import org.kesy.djob.sdu.api.model.ListenerParam;
import org.kesy.djob.sdu.api.task.TaskCallback;
import org.kesy.djob.sdu.api.task.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类<code>{@link JobListener4JobStoreFactory.get()}</code>  创建于 Jan 13, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class JobListener4JobStore implements JobListener {

	private static final Logger logger = LoggerFactory.getLogger(TaskCallback.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public boolean processExecuteAfter(ListenerParam param) {
		logger.debug("processExecuteAfter -> \n\t{}", param.toString());
		
		TaskInfo taskInfo = JobStoreFactory.get().selectTask(param.getTaskId());
		
		if (param.getTaskResult() != null) {
			TaskResult taskResult = param.getTaskResult();
			TaskInfo.Builder builder = TaskInfo.newBuilder(taskInfo);
			builder.setRunStatus(taskResult.getRunStatus());
			if (StringUtils.isNotEmpty(taskResult.getDecription())) {
				builder.setDescription(taskResult.getDecription());
			}
			if (StringUtils.isNotEmpty(taskResult.getException())) {
				builder.setException(taskResult.getException());
			}
			builder.setDataFrom(taskResult.getDataFrom());
			builder.setDataTo(taskResult.getDataTo());
			builder.setDataRow(taskResult.getDataRow());
			builder.setDataQuantity(taskResult.getDataQuantity());
			taskInfo = builder.build();
		}
		
		if (JobStoreFactory.get().updateTask(taskInfo)) {
			return JobStoreFactory.get()
					.updateJobStatusWhenNoRunningTask(param.getJobId());
		} else {
			return false;
		}
	}

	@Override
	public boolean processExecuteBefore(ListenerParam param) {
		logger.debug("processExecuteBefore -> \n\t{}", param.toString());
		
		TaskInfo taskInfo = JobStoreFactory.get().selectTask(param.getTaskId());
		if (taskInfo != null) {
			if (!JobStoreFactory.get()
					.updateTaskStatus(taskInfo.getId(), JobStatus.TASK_STATUS_RUNNING)) {
				return false;
			}
		}
		else {
			TaskInfo.Builder builder = TaskInfo.newBuilder();
			builder.setId(param.getTaskId());
			builder.setRunStatus(JobStatus.TASK_STATUS_RUNNING);
			builder.setNode(ClusterInfo.getNodeInfo());

			if (JobStoreFactory.get()
					.insertTask(builder.build(), param.getJobId()) == null) {
				return false;
			}
		}
		
		return JobStoreFactory.get()
				.updateJobStatus(param.getJobId(), JobStatus.JOB_STATUS_RUNNING);
	}

	@Override
	public boolean processExecuteException(ListenerParam param) {
		logger.debug("processExecuteException -> \n\t{}", param.toString());
		
		TaskInfo taskInfo = JobStoreFactory.get().selectTask(param.getTaskId());
		TaskResult taskResult = param.getTaskResult();
		TaskInfo.Builder builder = TaskInfo.newBuilder(taskInfo);
		
		if (param.getTaskResult() != null) {
			if (StringUtils.isNotEmpty(taskResult.getException())) {
				builder.setException(taskResult.getException());
			}
			if (StringUtils.isNotEmpty(taskResult.getDecription())) {
				builder.setDescription(taskResult.getDecription());
			}
		}
		builder.setRunStatus(JobStatus.TASK_STATUS_PROC_EXCEPTION);
		builder.setFinishTime(dateFormat.format(new Date()));
		taskInfo = builder.build();
		
		if (JobStoreFactory.get().updateTask(taskInfo)) {
			return JobStoreFactory.get()
					.updateJobStatus(param.getJobId(), JobStatus.JOB_STATUS_EXCEPTION);
		} else {
			return false;
		}
	}

	@Override
	public boolean processShutdownAfter(ListenerParam param) {
		logger.debug("processShutdownAfter -> \n\t{}", param.toString());
		
		return JobStoreFactory.get().updateJobStatus(param.getJobId(), JobStatus.JOB_STATUS_SHUTDOWN);
	}

	@Override
	public boolean processShutdownBefore(ListenerParam param) {
		return true;
	}

	@Override
	public boolean processShutdownException(ListenerParam param) {
		return true;
	}

	@Override
	public boolean processStopAfter(ListenerParam param) {
		return true;
	}

	@Override
	public boolean processStopBefore(ListenerParam param) {
		return true;
	}

	@Override
	public boolean processStopException(ListenerParam param) {
		return true;
	}

	@Override
	public boolean processInitAfter(ListenerParam param) {
		return true;
	}

	@Override
	public boolean processInitBefore(ListenerParam param) {
		return true;
	}

	@Override
	public boolean processInitException(ListenerParam param) {
		logger.debug("processInitException -> \n\t{}", param.toString());
		
		JobInfo jobInfo = JobStoreFactory.get().selectJob(param.getJobId());
		if (jobInfo != null) {
			JobInfo.Builder builder = JobInfo.newBuilder(jobInfo);
			if (StringUtils.isNotEmpty(param.getException())) {
				builder.setException(param.getException());
			}
			builder.setRunStatus(JobStatus.JOB_STATUS_EXCEPTION);
			return JobStoreFactory.get().updateJob(builder.build());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.JobListener#processWaitBefore(org.kesy.djob.sdu.api.model.ListenerParam)
	 */
	@Override
	public boolean processWaitBefore(ListenerParam param) {
		logger.debug("processWaitBefore -> \n\t{}", param.toString());
		
		TaskInfo.Builder builder = TaskInfo.newBuilder();
		builder.setId(param.getTaskId());
		builder.setRunStatus(JobStatus.TASK_STATUS_WAITING);
		builder.setNode(ClusterInfo.getNodeInfo());
		
		TaskInfo taskInfo = JobStoreFactory.get()
				.insertTask(builder.build(), param.getJobId());
		if (taskInfo != null) {
			return JobStoreFactory.get()
					.updateJobStatusToWaiting(param.getJobId());
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.JobListener#processWaitAfter(org.kesy.djob.sdu.api.model.ListenerParam)
	 */
	@Override
	public boolean processWaitAfter(ListenerParam param) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.JobListener#processWaitException(org.kesy.djob.sdu.api.model.ListenerParam)
	 */
	@Override
	public boolean processWaitException(ListenerParam param) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(Map<String, String> params) {
		// TODO implement JobListener.init
	}

}
