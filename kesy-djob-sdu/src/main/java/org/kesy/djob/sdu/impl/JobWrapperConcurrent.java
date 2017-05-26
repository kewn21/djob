/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.impl;

import java.util.UUID;

import org.kesy.djob.sdu.api.JobListener;
import org.kesy.djob.sdu.api.JobWrapperContext;
import org.kesy.djob.sdu.api.exception.JobRunTimeException;
import org.kesy.djob.sdu.api.model.ListenerParam;
import org.kesy.djob.sdu.concurrent.ConcurrentFactory;
import org.kesy.djob.sdu.concurrent.TaskRunnerImpl;
import org.kesy.djob.sdu.listener.ListenerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类<code>{@link JobWrapperConcurrent}</code> 创建于 Jan 6, 2014<br/>
 * 类描述:
 * <p>
 * <li></li>
 * 
 * @author kewn
 */
public class JobWrapperConcurrent extends AbstractJobWrapper {
	private static final long serialVersionUID = 3682018256335229935L;
	
	private static final Logger logger = LoggerFactory.getLogger(JobWrapperConcurrent.class);

	
	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.impl.JobWrapperAbstract#execute(java.lang.String, byte[])
	 */
	@Override
	public void execute(JobWrapperContext context) {
		try {
			String taskId = UUID.randomUUID().toString();
			if (!ConcurrentFactory.get().execute(
					new TaskRunnerImpl(jobId, taskId, execName, execParam, context))) {
				logger.info("\n\tThe task [{}] of job [{}] didn't run immediately, so put into the queue.", taskId, jobId);
				
				ListenerParam listenerParam = new ListenerParam();
				listenerParam.setJobId(jobId);
				listenerParam.setTaskId(taskId);
				for (JobListener process : ListenerRegistry.getListeners(execName)) {
					process.processWaitBefore(listenerParam);
				}
			}
		} catch (Exception e) {
			throw new JobRunTimeException(e);
		}
	}
}
