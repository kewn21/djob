/**
 * This file created at Jan 15, 2014.
 *
 */
package org.kesy.djob.sdu.api.model;

import java.io.Serializable;

import org.kesy.djob.sdu.api.task.TaskResult;
import org.kesy.djob.sdu.api.task.TaskStatus;

/**
 * 类<code>{@link ListenerParam}</code> 创建于 Jan 15, 2014<br/>
 * 类描述:
 * <p>
 * <li></li>
 * 
 * @author kewn
 */
public class ListenerParam implements Serializable, Cloneable {

	private static final long serialVersionUID = 898784838532160649L;

	private String jobId;

	private String taskId;

	private String execption;

	private TaskResult taskResult;

	private TaskStatus taskStatus;

	/**
	 * @return the jobId
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * @param jobId
	 *            the jobId to set
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId
	 *            the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the taskResult
	 */
	public TaskResult getTaskResult() {
		return taskResult;
	}

	/**
	 * @param taskResult
	 *            the taskResult to set
	 */
	public void setTaskResult(TaskResult taskResult) {
		this.taskResult = taskResult;
	}

	/**
	 * @return the taskStatus
	 */
	public TaskStatus getTaskStatus() {
		return taskStatus;
	}

	/**
	 * @param taskStatus
	 *            the taskStatus to set
	 */
	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}

	/**
	 * @return the execption
	 */
	public String getException() {
		return execption;
	}

	/**
	 * @param execption
	 *            the execption to set
	 */
	public void setException(String execption) {
		this.execption = execption;
	}

	@Override
	public String toString() {
		return new StringBuilder()
		.append("jobId:").append(jobId)
		.append(", taskId:").append(taskId)
		.append(", exception:").append(execption)
		.append(", taskResult:").append(taskResult)
		.append(", taskStatus:").append(taskStatus)
		.toString();
	}

}
