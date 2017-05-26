/**
 * This file created at Jan 18, 2014.
 *
 */
package org.kesy.djob.sdu.impl.quartz;

import java.io.Serializable;

import org.kesy.djob.sdu.api.task.TaskRunParam;

/**
 * 类<code>{@link JobParam}</code>  创建于 Jan 18, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class JobParam implements Serializable {

	private static final long serialVersionUID = -2782345153345939472L;
	
	private String jobId;
	private TaskRunParam param;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public TaskRunParam getParam() {
		return param;
	}

	public void setParam(TaskRunParam param) {
		this.param = param;
	}

}
