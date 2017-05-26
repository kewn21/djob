/**
 * This file created at Jan 14, 2014.
 *
 */
package org.kesy.djob.sdu.impl.quartz;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.kesy.djob.sdu.api.JobSchedulerConf;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类<code>{@link QuartzSchedulerFactory}</code>  创建于 Jan 14, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class QuartzSchedulerFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(QuartzSchedulerFactory.class);

	public static Scheduler getNewScheduler() {
		return getNewScheduler(JobSchedulerConf.TIMING_JOB_COUNT);
	}
	
	public static Scheduler getNewScheduler(int maxSize) {
		Scheduler scheduler = null;
		InputStream stream = null;
		try {
			StdSchedulerFactory std = new StdSchedulerFactory();
			
			stream = QuartzSchedulerFactory.class.getResourceAsStream("/config/quartz.properties");
			
			Properties props = new Properties();
			props.load(stream);
			props.put("org.quartz.threadPool.threadCount", String.valueOf(maxSize));
			std.initialize(props);
			
			scheduler = std.getScheduler();
		} catch (SchedulerException e) {
			logger.error("create new quartz Scheduler error : " + e.getMessage());
		} catch (IOException e) {
			logger.error("load properties of quartz Scheduler error : " + e.getMessage());
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
			}
		}
		return scheduler;
	}

}
