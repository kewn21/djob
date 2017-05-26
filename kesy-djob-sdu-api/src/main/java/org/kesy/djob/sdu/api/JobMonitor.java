/**
 * 2014年8月24日
 */
package org.kesy.djob.sdu.api;

import java.util.List;

import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfo;


/**
 * @author kewn
 *
 */
public interface JobMonitor {
	
	int searchJobMonitorCount(String jobName, String runStatus);
	
	List<JobInfo> searchJobMonitors(String jobName, String runStatus, int pageIndex, int pageSize);
	
	int getTaskMonitorCount(String jobId);
	
	List<TaskInfo> getTaskMonitors(String jobId, int pageIndex, int pageSize);
	
	int searchJobTaskMonitorCount(String jobName, String runStatus, String startTime, String endTime);
	
	List<TaskInfo> searchJobTaskMonitors(String jobName, String runStatus, String startTime, String endTime, int pageIndex, int pageSize);

}
