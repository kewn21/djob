package org.kesy.djob.sdu.api.task;


/**
 * <code>{@link TaskExecutor}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public interface TaskExecutor {

	String getJobId();
	
	void setJobId(String jobId);
	
	String getTaskId();
	
	void setTaskId(String taskId);
	
	String getName();
	
	void setName(String name);
	
	TaskStatus init(String execParam) throws Exception;

	void execute(TaskCallback callback) throws Exception;
	
	void run(TaskRunParam runParam, TaskCallback callback) throws Exception;

	void interrupt() throws Exception;
	
	void finish() throws Exception;

}