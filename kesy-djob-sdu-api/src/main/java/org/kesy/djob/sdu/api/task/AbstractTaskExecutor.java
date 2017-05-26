package org.kesy.djob.sdu.api.task;

public abstract class AbstractTaskExecutor implements TaskExecutor {

	protected String name;
	protected String jobId;
	protected String taskId;
	
	@Override
	public String getJobId() {
		return jobId;
	}
	
	@Override
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Override
	public String getTaskId() {
		return taskId;
	}
	
	@Override
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void interrupt() throws Exception {
	}

	@Override
	public void finish() throws Exception {
	}
}
