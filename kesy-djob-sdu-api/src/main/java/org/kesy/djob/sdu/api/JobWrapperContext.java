/**
 * 2015年2月3日
 */
package org.kesy.djob.sdu.api;

import java.util.Date;

import org.kesy.djob.sdu.api.task.TaskRunParam;

/**
 * @author kewn
 *
 */
public final class JobWrapperContext {
	
	private Date executeDate;
	private Date scheduleDate;
	private TaskRunParam param;
	
	public Date getExecuteDate() {
		return executeDate;
	}

	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public TaskRunParam getParam() {
		return param;
	}

	public void setParam(TaskRunParam param) {
		this.param = param;
	}

	public JobWrapperContext(Date executeDate, Date scheduleDate, TaskRunParam param) {
		this.executeDate = executeDate;
		this.scheduleDate = scheduleDate;
		this.param = param;
	}
}
