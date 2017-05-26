/**
 * This file created at 2014-3-7.
 *
 */
package org.kesy.djob.sdu.api;

import org.kesy.djob.core.spring.SpringFactory;


/**
 * 类<code>{@link JobManagerFactory}</code>  创建于 2014-3-7<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class JobManagerFactory {
	
	private static JobManager jobManager =  SpringFactory.getBean("jobManager", JobManager.class);
	
	public static JobManager get() {
		return jobManager;
	}

}
