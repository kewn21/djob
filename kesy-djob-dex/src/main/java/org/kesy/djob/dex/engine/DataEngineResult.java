/**
 * This file created at 2014-2-26.
 *
 */
package org.kesy.djob.dex.engine;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <code>{@link DataEngineResult}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class DataEngineResult {

	private String decription;
	private String exception = "";
	private String status;

	/**
	 * @desc:task begin time
	 */
	private Date beginTime;

	/**
	 * @desc:task end time
	 */
	private Date endTime;

	/**
	 * @desc:task tranfered line record count
	 */
	private long transferedLine = 0;

	private long totalPlanLine = 0;

	private long discardedRecords=0;
	/**
	 * @desc:task tranfered byte count
	 */
	private long byteCount = 0;

	public DataEngineResult() {
		setBeginTime(new Date());
	}

	public String getResInfo() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long timeElapsed = getTimeElapsed(getBeginTime(), getEndTime());
		return String.format(
				"\n" + "%-26s: %-18s\n" + 
				"%-26s: %-18s\n" + 
				"%-26s: %19s\n" + 
				"%-26s: %19s\n" + 
				"%-26s: %19s\n" + 
				"%-26s: %19s\n" + 
				"%-26s: %19d\n", 
				"DataX starts work at", df.format(getBeginTime()), 
				"DataX ends work at", df.format(getEndTime()), 
				"Total time costs", timeElapsed + "s",
				"Average byte speed", getSpeed(getByteCount(), timeElapsed),
				"Average line speed", getLineSpeed(getTransferedLine(),timeElapsed), 
				"Total transferred records", String.valueOf(getTransferedLine()),
				"Total discarded records", getDiscardedRecords());
	}

	public long getTimeElapsed(Date begin, Date end) {
		return (end.getTime() - begin.getTime()) / 1000;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception += exception;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the beginTime
	 */
	public Date getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime
	 *            the beginTime to set
	 */
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the byteCount
	 */
	public long getByteCount() {
		return byteCount;
	}

	/**
	 * @param byteCount
	 *            the byteCount to set
	 */
	public void setByteCount(long byteCount) {
		this.byteCount = byteCount;
	}

	public String getSpeed(long byteNum, long seconds) {
		if (seconds == 0) {
			seconds = 1;
		}
		long bytePerSecond = byteNum / seconds;
		long unit = bytePerSecond;
		if ((unit = bytePerSecond / 1000000) > 0) {
			return unit + "MB/s";
		} else if ((unit = bytePerSecond / 1000) > 0) {
			return unit + "KB/s";
		} else {
			if (byteNum > 0 && bytePerSecond <= 0) {
				bytePerSecond = 1;
			}
			return bytePerSecond + "B/s";
		}
	}

	/**
	 * Get average line speed
	 * 
	 * @param lines
	 *            Line amount
	 * @param seconds
	 *            Costed time.
	 * @return Average line speed.
	 */
	public String getLineSpeed(long lines, long seconds) {
		if (seconds == 0) {
			seconds = 1;
		}
		long linePerSecond = lines / seconds;

		if (lines > 0 && linePerSecond <= 0) {
			linePerSecond = 1;
		}

		return linePerSecond + "L/s";
	}

	/**
	 * @return the transferedLine
	 */
	public long getTransferedLine() {
		return transferedLine;
	}

	/**
	 * @param transferedLine
	 *            the transferedLine to set
	 */
	public void setTransferedLine(long transferedLine) {
		this.transferedLine = transferedLine;
	}

	/**
	 * @return the totalPlanLine
	 */
	public long getTotalPlanLine() {
		return totalPlanLine;
	}

	/**
	 * @param totalPlanLine
	 *            the totalPlanLine to set
	 */
	public void setTotalPlanLine(long totalPlanLine) {
		this.totalPlanLine = totalPlanLine;
	}

	/**
	 * @param discardedRecords the discardedRecords to set
	 */
	public void setDiscardedRecords(long discardedRecords) {
		this.discardedRecords = discardedRecords;
	}

	/**
	 * @return the discardedRecords
	 */
	public long getDiscardedRecords() {
		return discardedRecords;
	}

}
