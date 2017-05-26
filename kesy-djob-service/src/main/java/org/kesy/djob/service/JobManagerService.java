/**
 * 2014年9月7日
 */
package org.kesy.djob.service;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.kesy.djob.client.cmd.JobCmdFactory;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage.TaskMessage;
import org.kesy.djob.web.DataRequest;
import org.kesy.djob.web.DataResponse;
import org.springframework.stereotype.Service;

/**
 * @author kewn
 *
 */
@Service(value = "jobManager")
public class JobManagerService {
	
	public DataResponse saveJob(DataRequest request) {
		
		String jobId = MapUtils.getString(request.getParams(), "jobId");
		String name = MapUtils.getString(request.getParams(), "name");
		String startMode = MapUtils.getString(request.getParams(), "startMode");
		String execStrategy = MapUtils.getString(request.getParams(), "execStrategy");
		String execName = MapUtils.getString(request.getParams(), "execName");
		long timeout = MapUtils.getLongValue(request.getParams(), "timeout");
		String description = MapUtils.getString(request.getParams(), "description");
		
		JobMessage.Builder jobBuilder = JobMessage.newBuilder();
		jobBuilder.setName(name);
		jobBuilder.setStartMode(startMode);
		jobBuilder.setExecStrategy(execStrategy);
		if (StringUtils.isNotEmpty(description)) {
			jobBuilder.setDescription(description);
		}
		jobBuilder.setTimeout(timeout);

		TaskMessage.Builder taskBuilder = TaskMessage.newBuilder();
		taskBuilder.setExecName(execName);
		jobBuilder.setTaskMessage(taskBuilder);
		
	    DataResponse.DataResponseBuilder builder = DataResponse.newBuilder();
		if (StringUtils.isEmpty(jobId)) {
			builder.setResultValue(
					JobCmdFactory.createJob(jobBuilder.build()));
		} 
		else {
			jobBuilder.setId(jobId);
			builder.setResultValue(
					JobCmdFactory.updateJob(jobBuilder.build()));
		}
		
		return builder.build();
	}
	
	public DataResponse dropJob(DataRequest request){
		boolean isSuccessed = JobCmdFactory.dropJob(MapUtils.getString(request.getParams(), "jobId"));

		return DataResponse.newBuilder().setResultValue(isSuccessed).build();
	}
	
	public DataResponse dropJobs(DataRequest request){
		String jobIdsStr = MapUtils.getString(request.getParams(), "jobIds");
		if (StringUtils.isEmpty(jobIdsStr)) {
			return DataResponse.newBuilder().build();
		}
		
		String[] jobIds = jobIdsStr.split(",");
		boolean isSuccessed = JobCmdFactory.dropJobs(jobIds);

		return DataResponse.newBuilder().setResultValue(isSuccessed).build();
	}
}
