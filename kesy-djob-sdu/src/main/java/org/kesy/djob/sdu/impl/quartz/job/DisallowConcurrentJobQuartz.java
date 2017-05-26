/**
 * This file created at Jan 7, 2014.
 *
 */
package org.kesy.djob.sdu.impl.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

/**
 * 类<code>{@link JobQuartz}</code> 创建于 Jan 8, 2014<br/>
 * 类描述:不允许并发的job
 * <p>
 * <li></li>
 * 
 * @author kewn
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DisallowConcurrentJobQuartz extends JobQuartz {
	
}
