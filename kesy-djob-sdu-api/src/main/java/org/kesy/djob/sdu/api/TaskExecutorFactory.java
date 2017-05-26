/**
 * 2015年2月3日
 */
package org.kesy.djob.sdu.api;

import org.kesy.djob.sdu.api.exception.JobRunTimeException;
import org.kesy.djob.sdu.api.model.ListenerParam;
import org.kesy.djob.sdu.api.task.TaskExecutor;
import org.kesy.djob.sdu.api.task.TaskStatus;
import org.kesy.djob.sdu.listener.ListenerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kewn
 *
 */
public class TaskExecutorFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskExecutorFactory.class);
	
	public static TaskExecutor create(String jobId, String taskId,  String execName, String execParam) {
		TaskExecutor taskExecutor = null;
		
		ListenerParam listenerParam = new ListenerParam();
		listenerParam.setJobId(jobId);
		try {
			taskExecutor = ListenerRegistry.getTaskInstance(execName);
			taskExecutor.setJobId(jobId);
			taskExecutor.setName(execName);
			taskExecutor.setTaskId(taskId);
			TaskStatus status = taskExecutor.init(execParam);
			listenerParam.setTaskStatus(status);

			for (JobListener process : ListenerRegistry.getListeners(execName)) {
				process.processInitAfter(listenerParam);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			listenerParam.setException(e.getMessage());
			for (JobListener process : ListenerRegistry.getListeners(execName)) {
				process.processInitException(listenerParam);
			}
			throw new JobRunTimeException("JobWrapperAbstract#init error : " + e.getMessage(), e);
		}
		
		return taskExecutor;
	}

}
