package org.kesy.djob.sdu.api.task;

import org.kesy.djob.sdu.api.consts.JobStatus;

/**
 * <code>{@link TaskResult}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class TaskResult {

	private String decription;
	private String exception;
	private String runStatus;
	private long dataRow;
	private double dataQuantity;
	private long dataFrom;
	private long dataTo;
	private long rejectedRow;

	public TaskResult() {
		this(JobStatus.TASK_STATUS_FINISH);
	}
	
	public TaskResult(String runStatus) {
		this.runStatus = runStatus;
	}

	public String getDecription() {
		return decription;
	}

	public String getException() {
		return exception;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public long getDataRow() {
		return dataRow;
	}

	public double getDataQuantity() {
		return dataQuantity;
	}

	public long getDataFrom() {
		return dataFrom;
	}

	public long getDataTo() {
		return dataTo;
	}
	
	public long getRejectedRow() {
		return rejectedRow;
	}
	
	public static TaskResultBuilder createbBuilder() {
		return createbBuilder(JobStatus.TASK_STATUS_FINISH);
	}
	
	public static TaskResultBuilder createbBuilder(String runStatus) {
		return new TaskResultBuilder(runStatus);
	}

	public static final class TaskResultBuilder {
		
		private String decription;
		private String exception;
		private String runStatus;
		private long dataRow;
		private double dataQuantity;
		private long dataFrom;
		private long dataTo;
		private long rejectedRow;
		
		public TaskResultBuilder setDecription(String decription) {
			this.decription = decription;
			return this;
		}
		public TaskResultBuilder setException(String exception) {
			this.exception = exception;
			return this;
		}
		public TaskResultBuilder setRunStatus(String runStatus) {
			this.runStatus = runStatus;
			return this;
		}
		public TaskResultBuilder setDataRow(long dataRow) {
			this.dataRow = dataRow;
			return this;
		}
		public TaskResultBuilder setDataQuantity(double dataQuantity) {
			this.dataQuantity = dataQuantity;
			return this;
		}
		public TaskResultBuilder setDataFrom(long dataFrom) {
			this.dataFrom = dataFrom;
			return this;
		}
		public TaskResultBuilder setDataTo(long dataTo) {
			this.dataTo = dataTo;
			return this;
		}
		public TaskResultBuilder setRejectedRow(long rejectedRow) {
			this.rejectedRow = rejectedRow;
			return this;
		}
		
		public TaskResultBuilder() {
			this(JobStatus.TASK_STATUS_FINISH);
		}
		
		public TaskResultBuilder(String runStatus) {
			this.runStatus = runStatus;
		}
		
		public TaskResult build() {
			TaskResult result = new TaskResult();
			result.decription = decription;
			result.exception = exception;
			result.runStatus = runStatus;
			result.dataRow = dataRow;
			result.dataQuantity = dataQuantity;
			result.dataFrom = dataFrom;
			result.dataTo = dataTo;
			result.rejectedRow = rejectedRow;
			return result;
		}
	}
}