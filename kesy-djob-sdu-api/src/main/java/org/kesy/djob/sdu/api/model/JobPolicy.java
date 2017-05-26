/**
 * This file created at Jan 7, 2014.
 *
 */
package org.kesy.djob.sdu.api.model;

/**
 * 类<code>{@link JobPolicy}</code>  创建于 Jan 7, 2014<br/>
 * 类描述:job运行的策略<p>
 * 1、是否允许并发
 * 2、执行丢失处理规则（如不允许并发的情况下，某个job错过某次处理时间点的处理规则）
 * 3、job失败重做规则
 * @author kewn
 */
public class JobPolicy {

	class PolicyConstant {
		/**
		 * misfire，忽略错过的任务，concurrent为true，默认值
		 */
		public static final String MISFIRE_IGNORE = "MISFIRE_IGNORE";
		/**
		 * misfire，逐一重做错过的任务，concurrent为true
		 */
		public static final String MISFIRE_EACH_REDO = "MISFIRE_EACHE_REDO";

		/**
		 * failredo，等待手工重做失败任务，默认值
		 */
		public static final String FAILREDO_MANUAL = "FAILREDO_MANUAL";

		/**
		 *failredo， 系统自动重做失败任务
		 */
		public static final String FAILREDO_AUTO_REDO = "FAILREDO_AUTO_REDO";
	}

	/**
	 * 是否允许并发
	 */
	private boolean concurrent = true;

	/**
	 * 执行丢失处理
	 */
	private String misfire = PolicyConstant.MISFIRE_IGNORE;

	/**
	 * 失败处理
	 */
	private String failredo = PolicyConstant.FAILREDO_MANUAL;

	public boolean isConcurrent() {
		return concurrent;
	}

	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	public String getMisfire() {
		return misfire;
	}

	public void setMisfire(String misfire) {
		this.misfire = misfire;
	}

	public String getFailredo() {
		return failredo;
	}

	public void setFailredo(String failredo) {
		this.failredo = failredo;
	}
}
