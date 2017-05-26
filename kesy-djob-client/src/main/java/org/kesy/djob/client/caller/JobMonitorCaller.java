/**
 * 2014年8月24日
 */
package org.kesy.djob.client.caller;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kesy.djob.client.JobClientFactory;
import org.kesy.djob.rpc.SocketRpcController;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.StringType;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfo;
import org.kesy.djob.sdu.api.model.JobMonitorServiceProto.GetTaskMonitorsParam;
import org.kesy.djob.sdu.api.model.JobMonitorServiceProto.JobMonitorService;
import org.kesy.djob.sdu.api.model.JobMonitorServiceProto.JobMonitorService.BlockingInterface;
import org.kesy.djob.sdu.api.model.JobMonitorServiceProto.SearchJobMonitorsParam;

import com.google.protobuf.BlockingRpcChannel;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;

/**
 * @author kewn
 *
 */
public class JobMonitorCaller {

	private RpcController controller = new SocketRpcController();
	private BlockingInterface service;
	
    public JobMonitorCaller(){
    	BlockingRpcChannel channel = JobClientFactory.getChannel();
    	service = JobMonitorService.newBlockingStub(channel);
	}
    
    public int searchJobMonitorCount(String jobName, String runStatus) throws ServiceException {
    	SearchJobMonitorsParam.Builder builder = SearchJobMonitorsParam.newBuilder();
    	if (StringUtils.isNotEmpty(jobName)) {
    		builder.setJobName(jobName);
		}
    	if (StringUtils.isNotEmpty(runStatus)) {
    		builder.setRunStatus(runStatus);
		}
    	
    	return service.searchJobMonitorCount(controller, builder.build())
    			.getValue();
    }
    
    public List<JobInfo> searchJobMonitors(String jobName, String runStatus, int pageIndex, int pageSize) throws ServiceException {
    	SearchJobMonitorsParam.Builder builder = SearchJobMonitorsParam.newBuilder()
    			.setPageIndex(pageIndex)
    			.setPageSize(pageSize);
    	if (StringUtils.isNotEmpty(jobName)) {
    		builder.setJobName(jobName);
		}
    	if (StringUtils.isNotEmpty(runStatus)) {
    		builder.setRunStatus(runStatus);
		}
    	
    	return service.searchJobMonitors(controller, builder.build())
    			.getJobInfoList();
    }
	
    public int getTaskMonitorCount(String jobId) throws ServiceException {
    	StringType.Builder builder = StringType.newBuilder();
    	if (StringUtils.isNotEmpty(jobId)) {
    		builder.setValue(jobId);
		}
    	
    	return service.getTaskMonitorCount(controller, builder.build())
    			.getValue();
    }
	
    public List<TaskInfo> getTaskMonitors(String jobId, int pageIndex, int pageSize) throws ServiceException {
    	GetTaskMonitorsParam.Builder builder = GetTaskMonitorsParam.newBuilder()
    			.setPageIndex(pageIndex)
    			.setPageSize(pageSize);
    	if (StringUtils.isNotEmpty(jobId)) {
    		builder.setJobId(jobId);
		}
    	
    	return service.getTaskMonitors(controller, builder.build())
    			.getTaskInfoList();
    }
    
    public int searchJobTaskMonitorCount(String jobName, String runStatus, String startTime, String endTime) 
    		throws ServiceException {
    	SearchJobMonitorsParam.Builder builder = SearchJobMonitorsParam.newBuilder();
    	if (StringUtils.isNotEmpty(jobName)) {
    		builder.setJobName(jobName);
		}
    	if (StringUtils.isNotEmpty(runStatus)) {
    		builder.setRunStatus(runStatus);
		}
    	if (StringUtils.isNotEmpty(startTime)) {
    		builder.setStartTime(startTime);
		}
    	if (StringUtils.isNotEmpty(endTime)) {
    		builder.setEndTime(endTime);
		}
    	
    	return service.searchJobTaskMonitorCount(controller, builder.build())
    			.getValue();
    }
	
    public List<TaskInfo> searchJobTaskMonitors(String jobName, String runStatus, String startTime, String endTime
    		, int pageIndex, int pageSize) 
    		throws ServiceException {
    	SearchJobMonitorsParam.Builder builder = SearchJobMonitorsParam.newBuilder()
    			.setPageIndex(pageIndex)
    			.setPageSize(pageSize);
    	if (StringUtils.isNotEmpty(jobName)) {
    		builder.setJobName(jobName);
		}
    	if (StringUtils.isNotEmpty(runStatus)) {
    		builder.setRunStatus(runStatus);
		}
    	if (StringUtils.isNotEmpty(startTime)) {
    		builder.setStartTime(startTime);
		}
    	if (StringUtils.isNotEmpty(endTime)) {
    		builder.setEndTime(endTime);
		}
    	
    	return service.searchJobTaskMonitors(controller, builder.build())
    			.getTaskInfoList();
    }
}
