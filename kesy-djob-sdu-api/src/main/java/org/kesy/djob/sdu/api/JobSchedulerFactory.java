/**
 * This file created at 2014-3-13.
 *
 */
package org.kesy.djob.sdu.api;

import org.kesy.djob.core.spring.SpringFactory;
import org.kesy.djob.sdu.api.exception.JobException;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.task.TaskRunParam;

/**
 * 类<code>{@link JobSchedulerFactory}</code>  创建于 2014-3-13<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public abstract class JobSchedulerFactory {
	
	public static JobSchedulerFactory get() {
		return SpringFactory.getBean("jobSchedulerFactory", JobSchedulerFactory.class);
	}
	
	/**
	 * 功能描述:创建job</p>
	 * <li>1、解释jobInfo执行规则</li>
	 * <li>2、构建job</li>
	 * <li>3、启动job，放置等待执行队列</li>
	 * @param jobInfo
	 * @author kewn
	 * @throws JobException 
	 */
	public abstract boolean createJob(JobInfo jobInfo) throws JobException;

	public abstract boolean dropJob(String jobId);

	public abstract boolean restartJob(String jobId);
	
	public abstract boolean runJob(String jobId, TaskRunParam param);

	public abstract boolean shutdownJob(String jobId);

	public abstract boolean stopJob(String jobId);

	public abstract boolean updateJob(JobInfo jobInfo);
	
	public abstract boolean jobIsExist(String jobId);
}
