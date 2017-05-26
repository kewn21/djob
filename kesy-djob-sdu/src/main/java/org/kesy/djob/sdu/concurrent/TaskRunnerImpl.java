/**
 * 2014年8月26日
 */
package org.kesy.djob.sdu.concurrent;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.kesy.djob.sdu.api.JobListener;
import org.kesy.djob.sdu.api.JobWrapperContext;
import org.kesy.djob.sdu.api.TaskContext;
import org.kesy.djob.sdu.api.TaskExecutorFactory;
import org.kesy.djob.sdu.api.model.ListenerParam;
import org.kesy.djob.sdu.api.task.TaskCallback;
import org.kesy.djob.sdu.api.task.TaskExecutor;
import org.kesy.djob.sdu.api.task.TaskResult;
import org.kesy.djob.sdu.listener.ListenerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kewn
 *
 */
public class TaskRunnerImpl implements TaskRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskRunnerImpl.class);
	
	private String jobId;
	private String taskId;
	private String execName;
	private JobWrapperContext context;
	private TaskExecutor taskExecutor = null; 
	
	public TaskRunnerImpl(String jobId, String taskId, String execName, String execParam
			, JobWrapperContext context){
		this.jobId = jobId;
		this.taskId = taskId;
		this.execName = execName;
		this.context = context;
		
		taskExecutor = TaskExecutorFactory.create(jobId, taskId, execName, execParam);
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.concurrent.Runner#start()
	 */
	@Override
	public void start() throws Exception {
		final ListenerParam listenerParam = new ListenerParam();
		listenerParam.setJobId(jobId);
		listenerParam.setTaskId(taskId);
		
		final List<JobListener> listeners = ListenerRegistry.getListeners(execName);
		
		try {
			for (JobListener process : listeners) {
				process.processExecuteBefore(listenerParam);
			}
			
			TaskCallback callback = new TaskCallback() {
				@Override
				public void invoke(TaskResult taskResult) {
					if (taskResult == null) {
						throw new RuntimeException("TaskResult must be not null."); 
					}
					
					listenerParam.setTaskResult(taskResult);
					for (JobListener process : listeners) {
						process.processExecuteAfter(listenerParam);
					}
				}
			};
			
			TaskContext.setExecuteDate(new Date());
			TaskContext.setScheduleDate(context.getScheduleDate());
			TaskContext.setParam(context.getParam());
			
			logger.debug("In queue ExecuteTime : {}, ScheduledTime : {}"
					, TaskContext.getExecuteDate(), TaskContext.getScheduleDate());
			
			if (context.getParam() == null) {
				logger.info("taskExecutor.execute -> taskExecutor [{}]", taskExecutor);
				taskExecutor.execute(callback);
			}
			else {
				logger.info("taskExecutor.run -> param [{}], taskExecutor [{}]", context.getParam(), taskExecutor);
				taskExecutor.run(context.getParam(), callback);
			}
		} catch (Exception e) {
			logger.error("error execute AbstractRunner#execute," + e.getMessage(), e);
			
			listenerParam.setTaskResult(TaskResult.createbBuilder()
					.setException(ExceptionUtils.getFullStackTrace(e))
					.build());
			
			for (JobListener process : listeners) {
				process.processExecuteException(listenerParam);
			}
		} finally {
			try {
				taskExecutor.finish();
			} catch (Exception e) {
			} finally {
				logger.info("\n\tThe task [{}] of job [{}] has finished, then invokes ConcurrentFactory to run next task.", taskId, jobId);
				ConcurrentFactory.get().next(taskId);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.concurrent.Runner#stop()
	 */
	@Override
	public void stop() throws Exception {
		taskExecutor.interrupt();
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.concurrent.Runner#hold()
	 */
	@Override
	public void hold() throws Exception {
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.concurrent.Runner#getTaskId()
	 */
	@Override
	public String getTaskId() {
		return taskId;
	}
}
