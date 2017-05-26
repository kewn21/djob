/**
 * 2014年8月22日
 */
package org.kesy.djob.client.cmd;

import java.util.List;

import org.kesy.djob.client.caller.JobManagerCaller;
import org.kesy.djob.client.caller.JobMonitorCaller;
import org.kesy.djob.client.exception.JobManagerException;
import org.kesy.djob.client.exception.JobMonitorException;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfo;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ServiceException;

/**
 * @author kewn
 *
 */
public class JobCmdFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(JobCmdFactory.class);
	private static JobManagerCaller jobManagerCaller = new JobManagerCaller();
	private static JobMonitorCaller jobMonitorCaller = new JobMonitorCaller();
	
	public static boolean createJob(JobMessage jobMessage) {
		try {
			return jobManagerCaller.createJob(jobMessage).getIsSuccessed();
		} catch (ServiceException e) {
			logger.error("JobCmdFactory createJob error : " + e);
			throw new JobManagerException(e);
		}
	}
	
	public static boolean updateJob(JobMessage jobMessage) {
		try {
			return jobManagerCaller.updateJob(jobMessage).getIsSuccessed();
		} catch (ServiceException e) {
			logger.error("JobCmdFactory updateJob error : " + e);
			throw new JobManagerException(e);
		}
	}
	
	public static JobInfo findJob(String jobId) {
		try {
			return jobManagerCaller.findJob(jobId);
		} catch (ServiceException e) {
			logger.error("JobCmdFactory findJob error : " + e);
			throw new JobMonitorException(e);
		}
	}
	
	public static int searchJobMonitorCount(String jobName, String runStatus) {
		try {
			return jobMonitorCaller.searchJobMonitorCount(jobName, runStatus);
		} catch (ServiceException e) {
			logger.error("JobCmdFactory searchJobMonitorCount error : " + e);
			throw new JobMonitorException(e);
		}
    }
    
    public static List<JobInfo> searchJobMonitors(String jobName, String runStatus, int pageIndex, int pageSize) {
    	try {
			return jobMonitorCaller.searchJobMonitors(jobName, runStatus, pageIndex, pageSize);
		} catch (ServiceException e) {
			logger.error("JobCmdFactory searchJobMonitors error : " + e);
			throw new JobMonitorException(e);
		}
    }
	
    public static int getTaskMonitorCount(String jobId) {
    	try {
			return jobMonitorCaller.getTaskMonitorCount(jobId);
		} catch (ServiceException e) {
			logger.error("JobCmdFactory getTaskMonitorCount error : " + e);
			throw new JobMonitorException(e);
		}
    }
	
    public static List<TaskInfo> getTaskMonitors(String jobId, int pageIndex, int pageSize) {
    	try {
			return jobMonitorCaller.getTaskMonitors(jobId, pageIndex, pageSize);
		} catch (ServiceException e) {
			logger.error("JobCmdFactory getTaskMonitors error : " + e);
			throw new JobMonitorException(e);
		}
    }
    
    public static boolean runJob(String jobId, long dataFrom, long dataTo, int interval){
    	try {
			return jobManagerCaller.runJob(jobId, dataFrom, dataTo, interval).getIsSuccessed();
		} catch (ServiceException e) {
			logger.error("JobCmdFactory runJob error : " + e);
			throw new JobMonitorException(e);
		}
    }
    
    public static boolean runJobs(String[] jobIds, long dataFrom, long dataTo, int interval){
    	try {
			return jobManagerCaller.runJobs(jobIds, dataFrom, dataTo, interval).getIsSuccessed();
		} catch (ServiceException e) {
			logger.error("JobCmdFactory runJobs error : " + e);
			throw new JobMonitorException(e);
		}
    }
    
    public static boolean dropJob(String jobId){
    	try {
			return jobManagerCaller.dropJob(jobId).getIsSuccessed();
		} catch (ServiceException e) {
			logger.error("JobCmdFactory dropJob error : " + e);
			throw new JobMonitorException(e);
		}
    }
    
    public static boolean dropJobs(String[] jobIds){
    	try {
			return jobManagerCaller.dropJobs(jobIds).getIsSuccessed();
		} catch (ServiceException e) {
			logger.error("JobCmdFactory dropJobs error : " + e);
			throw new JobMonitorException(e);
		}
    }
    
    public static boolean startJob(String jobId){
    	try {
			return jobManagerCaller.startJob(jobId).getIsSuccessed();
		} catch (ServiceException e) {
			logger.error("JobCmdFactory startJob error : " + e);
			throw new JobMonitorException(e);
		}
    }

    public static boolean shutdownJob(String jobId){
    	try {
			return jobManagerCaller.shutdownJob(jobId).getIsSuccessed();
		} catch (ServiceException e) {
			logger.error("JobCmdFactory shutdownJob error : " + e);
			throw new JobMonitorException(e);
		}
    }
    
    public static int searchJobTaskMonitorCount(String jobName, String runStatus, String startTime, String endTime) {
		try {
			return jobMonitorCaller.searchJobTaskMonitorCount(jobName, runStatus, startTime, endTime);
		} catch (ServiceException e) {
			logger.error("JobCmdFactory searchJobTaskMonitorCount error : " + e);
			throw new JobMonitorException(e);
		}
    }
    
    public static List<TaskInfo> searchJobTaskMonitors(String jobName, String runStatus, String startTime, String endTime, int pageIndex, int pageSize) {
    	try {
			return jobMonitorCaller.searchJobTaskMonitors(jobName, runStatus, startTime, endTime, pageIndex, pageSize);
		} catch (ServiceException e) {
			logger.error("JobCmdFactory searchJobTaskMonitors error : " + e);
			throw new JobMonitorException(e);
		}
    }
}
