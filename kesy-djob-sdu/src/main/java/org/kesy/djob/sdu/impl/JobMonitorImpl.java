/**
 * 2014年8月24日
 */
package org.kesy.djob.sdu.impl;

import java.util.List;

import org.kesy.djob.sdu.api.JobMonitor;
import org.kesy.djob.sdu.api.JobStoreFactory;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfo;

/**
 * @author kewn
 *
 */
public class JobMonitorImpl implements JobMonitor {

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobInfo#searchJobInfoCount(java.lang.String, java.lang.String)
	 */
	@Override
	public int searchJobMonitorCount(String jobName, String runStatus) {
		return JobStoreFactory.get().searchJobInfoCount(jobName, runStatus);
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobInfo#searchJobInfos(java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public List<JobInfo> searchJobMonitors(String jobName, String runStatus, int pageIndex,
			int pageSize) {
		return JobStoreFactory.get().searchJobInfos(jobName, runStatus, pageIndex, pageSize);
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobInfo#getTaskInfoCount(java.lang.String)
	 */
	@Override
	public int getTaskMonitorCount(String jobId) {
		return JobStoreFactory.get().getTaskInfoCount(jobId);
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobInfo#getTaskInfos(java.lang.String, int, int)
	 */
	@Override
	public List<TaskInfo> getTaskMonitors(String jobId, int pageIndex, int pageSize) {
		return JobStoreFactory.get().getTaskInfos(jobId, pageIndex, pageSize);
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.JobMonitor#getJobTaskMonitorCount(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int searchJobTaskMonitorCount(String jobName, String runStatus,
			String startTime, String endTime) {
		return JobStoreFactory.get().searchJobTaskInfoCount(jobName, runStatus, startTime, endTime);
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.JobMonitor#getJobTaskMonitors(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public List<TaskInfo> searchJobTaskMonitors(String jobName, String runStatus,
			String startTime, String endTime, int pageIndex, int pageSize) {
		return JobStoreFactory.get().searchJobTaskInfos(jobName, runStatus, startTime, endTime, pageIndex, pageSize);
	}

}
