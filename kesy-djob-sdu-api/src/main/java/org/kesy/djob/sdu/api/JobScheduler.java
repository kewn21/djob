/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.api;

import org.kesy.djob.sdu.api.exception.JobException;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.task.TaskRunParam;

/**
 * 类<code>{@link JobScheduler}</code>  创建于 Jan 6, 2014<br/>
 * 类描述:QuartzJob操作接口<p>
 * <li>1、CRUD操作对应调度框架的job</li>
 * <li>2、start/restart/shutdown/run对应调度框架的job</li>
 * <li>3、检测对应调度框架的job状态，及本地job runStatus（状态检测不合并至JobManager的，主要基于作业自动重做的场景）</li>
 * @author kewn
 */
public interface JobScheduler {

	/**
	 * 功能描述:创建job</p>
	 * <li>1、解释jobInfo执行规则</li>
	 * <li>2、构建job</li>
	 * <li>3、启动job，放置等待执行队列</li>
	 * @param jobInfo
	 * @author kewn
	 * @throws JobException 
	 */
	boolean createJob(JobInfo jobInfo) throws JobException;

	/**
	 * 功能描述:删除job
	 * <li></li>
	 * @param jobId
	 * @return
	 * @author kewn
	 */
	boolean dropJob(String jobId);

	boolean restartJob(String jobId);

	boolean runJob(String jobId, TaskRunParam param);

	boolean shundownJob(String jobId);

	boolean stopJob(String jobId);

	boolean updateJob(JobInfo jobInfo);
	
	boolean jobIsExist(String jobId);
}
