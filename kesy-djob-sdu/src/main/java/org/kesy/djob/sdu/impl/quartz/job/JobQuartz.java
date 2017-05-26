/**
  * This file created at Jan 8, 2014.
 *

 */
package org.kesy.djob.sdu.impl.quartz.job;

import org.kesy.djob.sdu.api.JobWrapper;
import org.kesy.djob.sdu.api.JobWrapperContext;
import org.kesy.djob.sdu.impl.quartz.JobParam;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类<code>{@link JobQuartz}</code> 创建于 Jan 7, 2014<br/>
 * 类描述:由于Quartz不支持匿名job子类调用，JobQuartz为JobWrapper提供执行，允许并发
 * <p>
 * <li>1、执行job</li>
 * <li>2、中断job{@link JobQuartz#interrupt()}，job是executing即{@link JobQuartz#xecute(JobExecutionContext)}未返回前，才会调用interrupt</li>
 * 
 * @author kewn
 */
public class JobQuartz implements InterruptableJob {
	
	private static final Logger logger = LoggerFactory.getLogger(JobQuartz.class);

	volatile JobWrapper jobWrapper = null;
	volatile boolean isInterrupt = false;

	public JobQuartz() {
	}

	/* (non-Javadoc)
	 * 功能描述:执行
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 * @author kewn
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		jobWrapper = (JobWrapper) context.getMergedJobDataMap().get("jobWrapper");
		JobParam jobParam = (JobParam) context.getMergedJobDataMap().get("jobParam");
		if (!isInterrupt) {//这里无法确保的
			logger.debug("FireTime : {}, ScheduledFireTime : {}"
					, context.getFireTime(), context.getScheduledFireTime());
			
			JobWrapperContext wcontext = new JobWrapperContext(context.getFireTime()
					, context.getScheduledFireTime()
					, jobParam.getParam());
			
			jobWrapper.execute(wcontext);
			jobParam.setParam(null);//重置
		}
	}

	/*
	 * 中断 (non-Javadoc)
	 * 
	 * @see org.quartz.InterruptableJob#interrupt()
	 */
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		if (jobWrapper != null) {
			isInterrupt = true;
			jobWrapper.stop();
		}
	}

}

