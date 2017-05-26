/**
 * This file created at 2014-3-13.
 *
 */
package org.kesy.djob.sdu.impl.once;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kesy.djob.sdu.api.JobScheduler;
import org.kesy.djob.sdu.api.JobStoreFactory;
import org.kesy.djob.sdu.api.JobWrapper;
import org.kesy.djob.sdu.api.JobWrapperBuilder;
import org.kesy.djob.sdu.api.JobWrapperContext;
import org.kesy.djob.sdu.api.exception.JobException;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.task.TaskRunParam;

/**
 * <code>{@link JobSchedulerConcurrent4Once}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class JobSchedulerConcurrent4Once implements JobScheduler {
	
	private static ConcurrentMap<String, JobWrapper> jobWrapperMap = new ConcurrentHashMap<String, JobWrapper>();
	
	private static ExecutorService executorService = Executors.newCachedThreadPool();
	
	
	public JobSchedulerConcurrent4Once() {
	}

	
	protected boolean checkScheduler() {
		return true;
	}

	protected boolean checkJobInfoStatus(String jobId, String expectStatus) {
		JobInfo jobInfo = JobStoreFactory.get().selectJob(jobId);
		return checkJobInfoStatus(jobInfo, expectStatus);
	}

	protected boolean checkJobInfoStatus(JobInfo jobInfo, String expectStatus) {
		return expectStatus.equalsIgnoreCase(jobInfo.getRunStatus()); // 是否需要考虑quartz的状态
	}

	@Override
	public boolean createJob(JobInfo jobInfo) throws JobException {
		/*if (this.checkJobInfoStatus(jobInfo, Status.JOB_STATUS_RUNNING)) {
			throw new IllegalArgumentException("Wrong Job Status!");
		}
		
		if (jobInfo.getExecBuf() == null || jobInfo.getExecBuf().isEmpty()) {
			return false;
		}

		String jobId = jobInfo.getName();
		JobWrapper jobWrapper = JobWrapperBuilder.newBuilder(jobInfo.getExecName(), jobId,
				jobInfo.getExecBuf().toByteArray()).build();
		jobWrapper.init();*/
		
		return true;
	}

	@Override
	public boolean dropJob(String jobId) {
		return jobWrapperMap.remove(jobId) != null;
	}

	@Override
	public boolean restartJob(String jobId) {
		return true;
	}

	@Override
	public boolean runJob(final String jobId, final TaskRunParam param) {
		JobWrapper findedJobWrapper = null;
		if (jobWrapperMap.containsKey(jobId)) {
			findedJobWrapper = jobWrapperMap.get(jobId);
		}
		else {
			JobInfo jobInfo = JobStoreFactory.get().selectJob(jobId);
			findedJobWrapper = JobWrapperBuilder.newBuilder(jobInfo.getExecName(), 
							jobId, jobInfo.getExecParam()).build();
			jobWrapperMap.putIfAbsent(jobId, findedJobWrapper);
			findedJobWrapper.init();
		}
		
		
		if (findedJobWrapper != null) {
			final JobWrapper jobWrapper = findedJobWrapper;
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					Date nowDate = new Date();
					JobWrapperContext wcontext = new JobWrapperContext(nowDate
							, nowDate
							, param);
					
					jobWrapper.execute(wcontext);
				}
			});
		} 
		
		return true;
	}

	@Override
	public boolean shundownJob(String jobId) {
		return true;
	}

	@Override
	public boolean stopJob(String jobId) {
		return true;
	}

	@Override
	public boolean updateJob(JobInfo jobInfo) {
		return true;
	}


	@Override
	public boolean jobIsExist(String jobId) {
		return jobWrapperMap.containsKey(jobId);
	}
}
