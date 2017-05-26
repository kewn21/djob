/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.api;

import java.util.List;

import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfo;

/**
 * 类<code>{@link JobStore}</code>  创建于 Jan 6, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public interface JobStore {

	boolean deleteAll();

	boolean deleteJob(JobInfo job);

	boolean deleteJobs(String[] jobIds);

	JobInfo insertJob(JobInfo job);

	boolean updateJob(JobInfo job);
	
	JobInfo selectJob(String jobId);

	List<JobInfo> selectJobs();

	/**
	 * 功能描述:更新job的运行状态
	 * @param status
	 * @param jobId
	 * @return
	 * @author kewn
	 */
	boolean updateJobStatus(String jobId, String status);
	
	/**
	 * 当作业状态不为running和standby时，更改作业状态为waiting
	 * 功能描述:
	 * @param jobId
	 * @return
	 * @author kewn
	 */
	boolean updateJobStatusToWaiting(String jobId);
	
	/**
	 * 更改没有执行中任务的作业运行状态为waitting
	 * 功能描述:
	 * @param jobId
	 * @return
	 * @author kewn
	 */
	boolean updateJobStatusWhenNoRunningTask(String jobId);
	
	/**
	 * 功能描述:更新job的状态
	 * @param taskId
	 * @param runStatus
	 * @return
	 * @author kewn
	 */
	boolean updateTaskStatus(String taskId, String runStatus);
	
	/**
	 * 
	 * 功能描述:更改一个作业下所有状态为running的任务的状态为interrupt
	 * @param jobId
	 * @param status
	 * @return
	 * @author kewn
	 */
	boolean updateRunningTaskStatusToInterrupt(String jobId);
	
	/**
	 * 功能描述:更新job的启动方式
	 * @param jobId
	 * @param mode
	 * @return
	 * @author kewn
	 */
	boolean updateJobMode(String jobId, String mode);
	
	

	boolean updateTask(TaskInfo taskInfo);

	TaskInfo selectTask(String taskId);

	List<TaskInfo> selectTasks(String jobId);

	TaskInfo insertTask(TaskInfo taskInfo, String jobId);
	
	
	int searchJobInfoCount(String jobName, String runStatus);
	
	List<JobInfo> searchJobInfos(String jobName, String runStatus, int pageIndex, int pageSize);
	
	int getTaskInfoCount(String jobId);
	
	List<TaskInfo> getTaskInfos(String jobId, int pageIndex, int pageSize);
	
	
	int searchJobTaskInfoCount(String jobName, String runStatus, String startTime, String endTime);
	
	List<TaskInfo> searchJobTaskInfos(String jobName, String runStatus, String startTime, String endTime, int pageIndex, int pageSize);

}
