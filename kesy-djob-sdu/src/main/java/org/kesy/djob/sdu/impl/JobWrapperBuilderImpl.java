/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.impl;

import org.kesy.djob.sdu.api.JobWrapper;
import org.kesy.djob.sdu.api.JobWrapperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类<code>{@link JobWrapperBuilderImpl}</code>  创建于 Jan 6, 2014 <br/>
 * 类描述:初始化JobWrapper实例<p>
 * @author kewn
 */
public class JobWrapperBuilderImpl extends JobWrapperBuilder {

	protected static final Logger logger = LoggerFactory.getLogger(JobWrapperBuilderImpl.class);

	protected String execParam;
	protected String execName;
	protected String jobId;

	public JobWrapperBuilderImpl() {
	}

	@Override
	public JobWrapperBuilder createBuilder() {
		return new JobWrapperBuilderImpl();
	}

	@Override
	public JobWrapperBuilder createBuilder(String execName, String jobId, String execParam) {
		JobWrapperBuilder builder = createBuilder();
		builder.setJobId(jobId);
		builder.setExecName(execName);
		builder.setExecParam(execParam);
		return builder;
	}

	/**
	 * 功能描述:初始化JobWrapper及对应的TaskExecutor
	 * @return
	 * @author kewn
	 */
	@Override
	public JobWrapper build() {
		JobWrapper jobWrapper = new JobWrapperImpl();
		jobWrapper.setExecName(execName);
		jobWrapper.setJobId(jobId);
		jobWrapper.setExecParam(execParam);
		return jobWrapper;
	}

	@Override
	public void setExecParam(String execParam) {
		this.execParam = execParam;
	}

	@Override
	public void setExecName(String execName) {
		this.execName = execName;
	}

	@Override
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
