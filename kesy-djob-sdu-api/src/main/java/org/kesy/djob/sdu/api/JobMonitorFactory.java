/**
 * This file created at 2014-3-7.
 *

 */
package org.kesy.djob.sdu.api;

import org.kesy.djob.core.spring.SpringFactory;


/**
 * 类<code>{@link JobMonitorFactory}</code>  创建于 2014-3-7<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class JobMonitorFactory {
	
	private static JobMonitor jobMonitor =  SpringFactory.getBean("jobMonitor", JobMonitor.class);
	
	public static JobMonitor get() {
		return jobMonitor;
	}

}
