/**
 * This file created at Jan 7, 2014.
 *
 */
package org.kesy.djob.sdu.api.consts;

/**
 * 类<code>{@link JobStatus}</code>  创建于 Jan 7, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public final class JobStatus {

	// job状态
	public static final String	JOB_STATUS_WAITING			= "waiting";
	public static final String	JOB_STATUS_STANDBY			= "standby";
	public static final String	JOB_STATUS_RUNNING			= "running";
	public static final String	JOB_STATUS_SHUTDOWN			= "shutdown";
	public static final String	JOB_STATUS_EXCEPTION		= "exception";
	public static final String	JOB_STATUS_INTERRUPT		= "interrupt";

	// Job运行模式
	public static final String	JOB_MODE_AUTO				= "auto";
	public static final String	JOB_MODE_MANUAL				= "manual";
	public static final String	JOB_MODE_FORBIDDEN			= "forbidden";

	// 每次task执行状态
	public static final String	TASK_STATUS_WAITING			= "waiting";
	public static final String	TASK_STATUS_FINISH			= "finish";
	public static final String	TASK_STATUS_RUNNING			= "running";
	public static final String	TASK_STATUS_DATA_EXCEPTION	= "data_exception";
	public static final String	TASK_STATUS_PROC_EXCEPTION	= "proc_exception";
	public static final String	TASK_STATUS_STOP			= "stop";
	public static final String	TASK_STATUS_INTERRUPT		= "interrupt";

}
