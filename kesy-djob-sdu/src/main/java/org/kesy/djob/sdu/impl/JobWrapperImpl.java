/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
 * 类<code>{@link JobWrapperImpl}</code> 创建于 Jan 6, 2014<br/>
 * 类描述:
 * <p>
 * <li></li>
 * 
 * @author kewn
 */
public class JobWrapperImpl extends AbstractJobWrapper {
	private static final long serialVersionUID = 3682018256335229935L;
	
	private static final Logger logger = LoggerFactory.getLogger(JobWrapperImpl.class);
	
	//每个作业实例拥有一把锁，作业中的任务串行执行
	private Object lck = new Object();
	
	
	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.impl.JobWrapperAbstract#execute(java.lang.String, byte[])
	 */
	@Override
	public void execute(JobWrapperContext context) {
		String taskId = UUID.randomUUID().toString();
		TaskExecutor taskExecutor = TaskExecutorFactory.create(jobId, taskId, execName, execParam);
		localTaskExcutor = taskExecutor;
		
		final ListenerParam listenerParam = new ListenerParam();
		listenerParam.setJobId(jobId);
		listenerParam.setTaskId(taskId);

		final List<JobListener> listeners = ListenerRegistry.getListeners(execName);

		synchronized (lck) {
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
	}
}
