/**
 * This file created at Jan 7, 2014.
 *
 */
package org.kesy.djob.sdu.impl.quartz;

import org.kesy.djob.sdu.api.JobSchedulerConf;
import org.kesy.djob.sdu.api.exception.JobRunTimeException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类<code>{@link JobSchedulerConcurrent4Quartz}</code> 创建于 Jan 7, 2014<br/>
 * 类描述:jobOPerator的Quartz版本实现
 * <p>
 * <li></li>
 * 
 * @author kewn
 */
public class JobSchedulerConcurrent4Quartz extends JobScheduler4QuartzImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(JobSchedulerConcurrent4Quartz.class);
	
	
	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.impl.quartz.JobScheduler4QuartzImpl#createScheduler()
	 */
	@Override
	protected void createScheduler() {
		try {
			scheduler = QuartzSchedulerFactory.getNewScheduler(JobSchedulerConf.MAX_JOB_COUNT);
			
			if (!scheduler.isStarted()) {
				logger.info("JobScheduler4QuartzImpl scheduler start");
				scheduler.start();
			}
		} catch (SchedulerException e) {
			logger.error("JobScheduler4QuartzImpl init error : " + e.getMessage(), e);
			throw new JobRunTimeException("JobScheduler4QuartzImpl init error : " + e.getMessage(), e);
		}
	}
}
