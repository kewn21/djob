/**
 * This file created at Jan 11, 2014.
 *
 */
package org.kesy.djob.sdu.api.task;


/**
 * 类<code>{@link TaskCallback}</code>  创建于 Jan 11, 2014<br/>
 * 类描述:客户端任务执行器{@link TaskExecutor}向调度器报告自身执行情况接口<p>
 * <li></li>
 * @author kewn
 */
public interface TaskCallback {

	/**
	 * 功能描述:通知server，job自身的执行情况
	 * @return 是否成功通知server
	 * @author kewn
	 */
	void invoke(TaskResult taskResult);
	

	/**
	 * 功能描述:设置TaskResult对应的taskinfo，不支持客户端调用
	 * @param taskId
	 * @return
	 * @author kewn
	 
	void setTaskId(String taskId);*/
}
