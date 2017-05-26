/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.api;

import org.kesy.djob.core.spring.SpringFactory;

/**
 * 类<code>{@link JobWrapperBuilder}</code>  创建于 Jan 6, 2014 <br/>
 * 类描述:初始化JobWrapper实例<p>
 * @author kewn
 */
public abstract class JobWrapperBuilder {
	
	private static JobWrapperBuilder getInstance() {
		return SpringFactory.getBean("jobWrapperBuilder", JobWrapperBuilder.class);
	}
	
	public static JobWrapperBuilder newBuilder() {
		return getInstance().createBuilder();
	}

	public static JobWrapperBuilder newBuilder(String execName, String jobId, String execParam) {
		return getInstance().createBuilder(execName, jobId, execParam);
	}
	
	
	public abstract JobWrapperBuilder createBuilder();

	public abstract JobWrapperBuilder createBuilder(String execName, String jobId, String execParam);
	
	public abstract void setExecParam(String execParam);

	public abstract void setExecName(String execName);

	public abstract void setJobId(String jobId);
	
	public abstract JobWrapper build();

}
