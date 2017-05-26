/**
 * This file created at 2014-2-13.
 *
 */
package org.kesy.djob.service.model;

/**
 * <code>{@link TaskMonitorInfo}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class TaskMonitorInfo {
	
    private String id;
	private String jobId;
	private String startTime;
	private String finishTime;
	private String runStatus;
	private String description;
	private String exception;
	private long version;
	private long dataFrom;
	private long dataTo;
	private long dataRow;
	private double dataQuantity;
	private long rejectedRow;
	private boolean isTimeout;
	private String node;
	private String createTime;
	private String lastModifiedTime;
	
	private String jobName;
    private String startMode;
    private String execStrategy;
    private String execName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getRunStatus() {
		return runStatus;
	}
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
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
	public long getDataRow() {
		return dataRow;
	}
	public void setDataRow(long dataRow) {
		this.dataRow = dataRow;
	}
	public double getDataQuantity() {
		return dataQuantity;
	}
	public void setDataQuantity(double dataQuantity) {
		this.dataQuantity = dataQuantity;
	}
	public long getRejectedRow() {
		return rejectedRow;
	}
	public void setRejectedRow(long rejectedRow) {
		this.rejectedRow = rejectedRow;
	}
	public boolean isTimeout() {
		return isTimeout;
	}
	public void setTimeout(boolean isTimeout) {
		this.isTimeout = isTimeout;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getStartMode() {
		return startMode;
	}
	public void setStartMode(String startMode) {
		this.startMode = startMode;
	}
	public String getExecStrategy() {
		return execStrategy;
	}
	public void setExecStrategy(String execStrategy) {
		this.execStrategy = execStrategy;
	}
	public String getExecName() {
		return execName;
	}
	public void setExecName(String execName) {
		this.execName = execName;
	}
}
