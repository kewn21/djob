/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.impl;

import org.kesy.djob.sdu.api.JobWrapper;
import org.kesy.djob.sdu.api.JobWrapperBuilder;

/**
 * 类<code>{@link JobWrapperConcurrentBuilder}</code>  创建于 Jan 6, 2014 <br/>
 * 类描述:初始化JobWrapper实例<p>
 * @author kewn
 */
public class JobWrapperConcurrentBuilder extends JobWrapperBuilderImpl {
	
	public JobWrapperConcurrentBuilder() {
	}
	
	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.impl.JobWrapperBuilderImpl#createBuilder()
	 */
	@Override
	public JobWrapperBuilder createBuilder() {
		return new JobWrapperConcurrentBuilder();
	}

	/**
	 * 功能描述:初始化JobWrapper及对应的TaskExecutor
	 * @return
	 * @author kewn
	 */
	@Override
	public JobWrapper build() {
		JobWrapper jobWrapper = new JobWrapperConcurrent();
		jobWrapper.setExecName(execName);
		jobWrapper.setJobId(jobId);
		jobWrapper.setExecParam(execParam);
		return jobWrapper;
	}

}
