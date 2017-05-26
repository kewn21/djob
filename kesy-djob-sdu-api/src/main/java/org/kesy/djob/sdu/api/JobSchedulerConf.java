/**
 * This file created at 2014年6月5日.
 *
 */
package org.kesy.djob.sdu.api;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 类<code>{@link JobSchedulerConf}</code>  创建于 2014年6月5日<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class JobSchedulerConf {
	
	private static final Log log = LogFactory.getLog(JobSchedulerConf.class);
	
	public static int TIMING_JOB_COUNT;
	public static int RUNONCE_JOB_COUNT;
	
	public static int MAX_JOB_COUNT;
	public static int ACTIVE_JOB_COUNT;
	
	static {
		InputStream stream = JobSchedulerConf.class.getResourceAsStream("/job/job-scheduler.properties");
		Properties props = new Properties();
		try {
			props.load(stream);
			TIMING_JOB_COUNT = Integer.valueOf(props.getProperty("timing.job.count", "10"));
			RUNONCE_JOB_COUNT = Integer.valueOf(props.getProperty("runonce.job.count", "10"));
			
			MAX_JOB_COUNT = Integer.valueOf(props.getProperty("max.job.count", "1000"));
			ACTIVE_JOB_COUNT = Integer.valueOf(props.getProperty("active.job.count", "10"));
		} catch (Exception e) {
			log.error("init JobSchedulerConf fail : " + e.getMessage());
		}
	}

}
