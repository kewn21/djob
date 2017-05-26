/**
 * This file created at Jan 6, 2014.
 *
 */
package org.kesy.djob.sdu.api;

import java.util.List;

import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage;
import org.kesy.djob.sdu.api.task.TaskRunParam;

/**
 * 类<code>{@link JobManager}</code>  创建于 Jan 6, 2014<br/>
 * 类描述:业务作业管理接口<p>
 * <li>1、各类操作{@link JobStore}本地作业记录及{@link JobScheduler}实际调度框架记录</li>
 * <li>2、检测{@link JobStore}本地作业运行模式startMode</li>
 * @author kewn
 */
public interface JobManager {

	boolean createJob(JobMessage job);
	
	boolean dropAll();

	boolean dropJob(String jobId);

	boolean dropJobs(String[] jobIds);

	JobInfo findJob(String jobId);

	List<JobInfo> listJobs();

	boolean restartAll();

	boolean restartJob(String jobId);

	boolean restartJobs(String[] jobIds);

	boolean runJob(String jobId, TaskRunParam param);
	
	boolean runJobs(String[] jobIds, TaskRunParam param);

	boolean shutdownAll();

	boolean shutdownJob(String jobId);

	boolean shutdownJobs(String[] jobIds);

	boolean startAll();

	boolean startJob(String jobId);

	boolean startJobs(String[] jobIds);

	boolean stopAll();

	boolean stopJob(String jobId);

	boolean stopJobs(String[] jobIds);

	boolean updateJob(JobMessage job);
	
	boolean updateJobStatus(String jobId, String status);
	
	boolean updateJobStatusWhenNotRunning(String jobId);
	
	boolean updateTaskStatus(String taskId, String status);
	
	boolean jobIsExist(String jobId);

}
