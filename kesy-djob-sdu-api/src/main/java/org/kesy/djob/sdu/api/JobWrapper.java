/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.api;

import java.io.Serializable;

import org.kesy.djob.sdu.api.task.TaskExecutor;

/**
 * 类<code>{@link JobWrapper}</code> 创建于 Jan 6, 2014<br/>
 * 类描述:任务执行器
 * <p>
 * <li>1、初始化{@link TaskExecutor}</li>
 * <li>2、</li>
 * <li>3、</li>
 * 
 * @author kewn
 */
public interface JobWrapper extends Serializable, Cloneable {

	/**
	 * 功能描述:初始化job的TaskExecutor信息
	 * @author kewn
	 */
	void init();

	/**
	 * 功能描述:执行job
	 * @param context 一次run的上下文
	 * @author kewn
	 */
	void execute(JobWrapperContext context);

	/**
	 * 功能描述:如果job正在调度中执行，将停止job的该次执行，下次仍会继续
	 * @author kewn
	 */
	void stop();

	void setExecParam(String execParam);

	void setExecName(String execName);

	void setJobId(String jobId);

	String getJobId();
}
