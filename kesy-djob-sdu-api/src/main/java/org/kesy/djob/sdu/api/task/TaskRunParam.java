/**
 * 2014年9月2日
 */
package org.kesy.djob.sdu.api.task;

/**
 * @author kewn
 *
 */
public class TaskRunParam {
	
	private long dataFrom;
	private long dataTo;
	private int interval = 1;
	
	public TaskRunParam(long dataFrom, long dataTo, int interval) {
		this.dataFrom = dataFrom;
		this.dataTo = dataTo;
		this.interval = interval;
	}
	
	public long getDataFrom() {
		return dataFrom;
	}
	public void setDataFrom(long dataFrom) {
		this.dataFrom = dataFrom;
	}
	public long getDataTo() {
		return dataTo;
	}
	public void setDataTo(long dataTo) {
		this.dataTo = dataTo;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}

}
