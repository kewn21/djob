/**
 * This file created at Jan 13, 2014.
 *
 */
package org.kesy.djob.sdu.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.kesy.djob.sdu.api.JobListener;
import org.kesy.djob.sdu.api.JobWrapper;
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
 * 类<code>{@link AbstractJobWrapper}</code>  创建于 Jan 13, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public abstract class AbstractJobWrapper implements JobWrapper {
	private static final long serialVersionUID = -8497428638613024714L;
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractJobWrapper.class);
	
	protected String jobId;
	protected String execName;
	protected String execParam;
	
	protected TaskExecutor localTaskExcutor = null;

	@Override
	public void init() {
	}

	@Override
	public void execute(JobWrapperContext context) {
		String taskId = UUID.randomUUID().toString();
		TaskExecutor taskExecutor = TaskExecutorFactory.create(jobId, taskId, execName, execParam);
		localTaskExcutor = taskExecutor;
		
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
			
			if (context.getParam() == null) {
				taskExecutor.execute(callback);
			}
			else {
				taskExecutor.run(context.getParam(), callback);
			}
		} catch (Exception e) {
			logger.error("error execute AbstractJobWrapper#execute," + e.getMessage(), e);
			
			listenerParam.setTaskResult(TaskResult.createbBuilder()
					.setException(e.getMessage()).build());
			
			for (JobListener process : listeners) {
				process.processExecuteException(listenerParam);
			}
		} finally {
			try {
				taskExecutor.finish();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void stop() {
		ListenerParam listenerParam = new ListenerParam();
		listenerParam.setJobId(jobId);
		for (JobListener process : ListenerRegistry.getListeners(execName)) {
			process.processStopBefore(listenerParam);
		}

		try {
			//only stop the last task
			localTaskExcutor.interrupt();
		} catch (Exception e) {
		} finally {
			for (JobListener process : ListenerRegistry.getListeners(execName)) {
				process.processStopAfter(listenerParam);
			}
		} 
	}

	@Override
	public String getJobId() {
		return this.jobId;
	}

	@Override
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Override
	public void setExecName(String execName) {
		this.execName = execName;
	}

	@Override
	public void setExecParam(String execParam) {
		this.execParam = execParam;
	}
}
