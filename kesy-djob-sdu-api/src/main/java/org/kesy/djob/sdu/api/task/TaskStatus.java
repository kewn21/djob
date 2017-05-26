/**
 * This file created at 2014-1-6.
 *
 */
package org.kesy.djob.sdu.api.task;

/**
 * <code>{@link TaskStatus}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class TaskStatus {

	private boolean beready;
	private String exception;

	public TaskStatus() {
		this(true);
	}
	
	public TaskStatus(boolean beready) {
		this.beready = beready;
	}

	public boolean isBeready() {
		return beready;
	}

	public String getException() {
		return exception;
	}
	
	public static TaskStatusBuilder createBuilder() {
		return createBuilder(true);
	}
	
	public static TaskStatusBuilder createBuilder(boolean beready) {
		return new TaskStatusBuilder(beready);
	}
	
	public static final class TaskStatusBuilder {
		private boolean beready;
		private String exception;
		
		public TaskStatusBuilder() {
			this(true);
		}
		
		public TaskStatusBuilder(boolean beready) {
			this.beready = beready;
		}

		public boolean isBeready() {
			return beready;
		}
		

		public TaskStatusBuilder setBeready(boolean beready) {
			this.beready = beready;
			return this;
		}

		public TaskStatusBuilder setException(String exception) {
			this.exception = exception;
			return this;
		}
		
		public TaskStatus build(){
			TaskStatus status = new TaskStatus();
			status.beready = beready;
			status.exception = exception;
			return status;
		}
	}
}
