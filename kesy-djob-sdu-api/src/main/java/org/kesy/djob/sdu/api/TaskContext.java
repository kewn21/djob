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
public final class TaskContext {
	
	private static ThreadLocal<Date> localExecuteDate = new ThreadLocal<Date>();
	private static ThreadLocal<Date> localScheduleDate = new ThreadLocal<Date>();
	private static ThreadLocal<TaskRunParam> localParam = new ThreadLocal<TaskRunParam>();
	
	public static ThreadLocal<Date> getExecuteDate() {
		return localExecuteDate;
	}
	public static ThreadLocal<Date> getScheduleDate() {
		return localScheduleDate;
	}
	public static ThreadLocal<TaskRunParam> getParam() {
		return localParam;
	}
	
	public static void setExecuteDate(Date executeDate) {
		localExecuteDate.set(executeDate);
	}
	public static void setScheduleDate(Date scheduleDate) {
		localScheduleDate.set(scheduleDate);
	}
	public static void setParam(TaskRunParam param) {
		localParam.set(param);
	}
}
