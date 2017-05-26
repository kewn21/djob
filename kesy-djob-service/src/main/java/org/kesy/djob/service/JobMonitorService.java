package org.kesy.djob.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.kesy.djob.client.cmd.JobCmdFactory;
import org.kesy.djob.sdu.api.consts.JobStatus;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfo;
import org.kesy.djob.service.model.JobMonitorInfo;
import org.kesy.djob.service.model.TaskMonitorInfo;
import org.kesy.djob.web.DataRequest;
import org.kesy.djob.web.DataResponse;
import org.springframework.stereotype.Service;

@Service(value = "jobMonitor")
public class JobMonitorService {
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public DataResponse findJob(DataRequest request) throws ParseException {
		JobInfo jobInfo = JobCmdFactory.findJob(MapUtils.getString(request.getParams(), "jobId"));
		return DataResponse.newBuilder().setResultValue(makeToJobMonitorInfo(jobInfo)).build();
	}

	public DataResponse searchJobMonitorCount(DataRequest request) {
		int count = JobCmdFactory.searchJobMonitorCount(MapUtils.getString(request.getParams(), "jobName")
				, MapUtils.getString(request.getParams(), "runStatus"));
		
		return DataResponse.newBuilder().setResultValue(count).build();
	}
	
	public DataResponse searchJobMonitors(DataRequest request) throws ParseException {
		List<JobInfo> jobInfos = JobCmdFactory.searchJobMonitors(MapUtils.getString(request.getParams(), "jobName")
				, MapUtils.getString(request.getParams(), "runStatus")
				, MapUtils.getIntValue(request.getParams(), "pageIndex", 0)
				, MapUtils.getIntValue(request.getParams(), "pageSize", 0));
		
		List<JobMonitorInfo> jobMonitorInfos = new ArrayList<JobMonitorInfo>();
		if (jobInfos != null && jobInfos.size() > 0) {
			for (JobInfo jobInfo : jobInfos) {
				jobMonitorInfos.add(makeToJobMonitorInfo(jobInfo));
			}
		}
		
		return DataResponse.newBuilder().setResultValue(jobMonitorInfos).build();
	}
	
	public DataResponse getTaskMonitorCount(DataRequest request) {
		int count = JobCmdFactory.getTaskMonitorCount(
				MapUtils.getString(request.getParams(), "jobId"));
		
		return DataResponse.newBuilder().setResultValue(count).build();
	}
	
	public DataResponse getTaskMonitors(DataRequest request) throws ParseException {
		List<TaskInfo> taskInfos = JobCmdFactory.getTaskMonitors(MapUtils.getString(request.getParams(), "jobId")
				, MapUtils.getIntValue(request.getParams(), "pageIndex", 0)
				, MapUtils.getIntValue(request.getParams(), "pageSize", 0));
		
		List<TaskMonitorInfo> taskMonitorInfos = new ArrayList<TaskMonitorInfo>();
		if (taskInfos != null && taskInfos.size() > 0) {
			for (TaskInfo taskInfo : taskInfos) {
				taskMonitorInfos.add(makeToTaskMonitorInfo(taskInfo));
			}
		}
		
		return DataResponse.newBuilder().setResultValue(taskMonitorInfos).build();
	}
	
	
	public DataResponse runJob(DataRequest request){
		boolean isSuccessed = JobCmdFactory.runJob(MapUtils.getString(request.getParams(), "jobId")
				, MapUtils.getLong(request.getParams(), "dataFrom")
				, MapUtils.getLong(request.getParams(), "dataTo")
				, MapUtils.getIntValue(request.getParams(), "interval"));

		return DataResponse.newBuilder().setResultValue(isSuccessed).build();
	}
	
	public DataResponse runJobs(DataRequest request){
		String jobIdsStr = MapUtils.getString(request.getParams(), "jobIds");
		if (StringUtils.isEmpty(jobIdsStr)) {
			return DataResponse.newBuilder().build();
		}
		
		String[] jobIds = jobIdsStr.split(",");
		boolean isSuccessed = JobCmdFactory.runJobs(jobIds
				, MapUtils.getLong(request.getParams(), "dataFrom")
				, MapUtils.getLong(request.getParams(), "dataTo")
				, MapUtils.getIntValue(request.getParams(), "interval"));

		return DataResponse.newBuilder().setResultValue(isSuccessed).build();
	}
	
	public DataResponse startJob(DataRequest request){
		boolean isSuccessed = JobCmdFactory.startJob(MapUtils.getString(request.getParams(), "jobId"));
		return DataResponse.newBuilder().setResultValue(isSuccessed).build();
	}
	
	public DataResponse shutdownJob(DataRequest request){
		boolean isSuccessed = JobCmdFactory.shutdownJob(MapUtils.getString(request.getParams(), "jobId"));
		return DataResponse.newBuilder().setResultValue(isSuccessed).build();
	}
	
	public DataResponse searchJobTaskMonitorCount(DataRequest request) {
		int count = JobCmdFactory.searchJobTaskMonitorCount(MapUtils.getString(request.getParams(), "jobName")
				, MapUtils.getString(request.getParams(), "runStatus")
				, null, null);
		
		return DataResponse.newBuilder().setResultValue(count).build();
	}
	
	public DataResponse searchJobTaskMonitors(DataRequest request) throws ParseException {
		List<TaskInfo> tasInfos = JobCmdFactory.searchJobTaskMonitors(MapUtils.getString(request.getParams(), "jobName")
				, MapUtils.getString(request.getParams(), "runStatus")
				, null, null
				, MapUtils.getIntValue(request.getParams(), "pageIndex", 0)
				, MapUtils.getIntValue(request.getParams(), "pageSize", 0));
		
		List<TaskMonitorInfo> taskMonitorInfos = new ArrayList<TaskMonitorInfo>();
		if (tasInfos != null && tasInfos.size() > 0) {
			for (TaskInfo jobInfo : tasInfos) {
				taskMonitorInfos.add(makeToTaskMonitorInfo(jobInfo));
			}
		}
		
		return DataResponse.newBuilder().setResultValue(taskMonitorInfos).build();
	}
	
	public DataResponse searchJobTaskTodayMonitorCount(DataRequest request) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String time = formatter.format(new Date());
		
		int count = JobCmdFactory.searchJobTaskMonitorCount(MapUtils.getString(request.getParams(), "jobName")
				, JobStatus.TASK_STATUS_FINISH
				, time, time);
		
		return DataResponse.newBuilder().setResultValue(count).build();
	}
	
	public DataResponse searchJobTaskTodayMonitors(DataRequest request) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String time = formatter.format(new Date());
		
		List<TaskInfo> tasInfos = JobCmdFactory.searchJobTaskMonitors(MapUtils.getString(request.getParams(), "jobName")
				, JobStatus.TASK_STATUS_FINISH
				, time, time
				, MapUtils.getIntValue(request.getParams(), "pageIndex", 0)
				, MapUtils.getIntValue(request.getParams(), "pageSize", 0));
		
		List<TaskMonitorInfo> taskMonitorInfos = new ArrayList<TaskMonitorInfo>();
		if (tasInfos != null && tasInfos.size() > 0) {
			for (TaskInfo jobInfo : tasInfos) {
				taskMonitorInfos.add(makeToTaskMonitorInfo(jobInfo));
			}
		}
		
		return DataResponse.newBuilder().setResultValue(taskMonitorInfos).build();
	}
	

	private JobMonitorInfo makeToJobMonitorInfo(JobInfo jobInfo) throws ParseException{
		JobMonitorInfo jobMonitorInfo = new JobMonitorInfo();
		
		jobMonitorInfo.setId(jobInfo.getId());
		jobMonitorInfo.setName(jobInfo.getName());
		jobMonitorInfo.setDescription(jobInfo.getDescription());
		jobMonitorInfo.setException(jobInfo.getException());
		jobMonitorInfo.setExecName(jobInfo.getExecName());
		jobMonitorInfo.setExecStrategy(jobInfo.getExecStrategy());
		jobMonitorInfo.setRunStatus(jobInfo.getRunStatus());
		jobMonitorInfo.setStartMode(jobInfo.getStartMode());
		
		jobMonitorInfo.setTimeout(jobInfo.getTimeout());
		jobMonitorInfo.setLabel(jobInfo.getLabel());
		
		jobMonitorInfo.setCreator(jobInfo.getCreator());
		jobMonitorInfo.setPrincipal(jobInfo.getPrincipal());
		
		if(StringUtils.isNotEmpty(jobInfo.getCreateTime())){
			Date date = formatter.parse(jobInfo.getCreateTime());
			jobMonitorInfo.setCreateTime(formatter.format(date));
		}
		if(StringUtils.isNotEmpty(jobInfo.getLastModifiedTime())){
			Date date = formatter.parse(jobInfo.getLastModifiedTime());
			jobMonitorInfo.setLastModifiedTime(formatter.format(date));
		}
		
		return jobMonitorInfo;
	}
	
	private TaskMonitorInfo makeToTaskMonitorInfo(TaskInfo taskInfo) throws ParseException{
		TaskMonitorInfo taskMonitorInfo = new TaskMonitorInfo();

		taskMonitorInfo.setId(taskInfo.getId());
		taskMonitorInfo.setJobId(taskInfo.getJobId());

		if(StringUtils.isNotEmpty(taskInfo.getStartTime())){
			Date date = formatter.parse(taskInfo.getStartTime());
		    taskMonitorInfo.setStartTime(formatter.format(date));
		}
		if(StringUtils.isNotEmpty(taskInfo.getFinishTime())){
			Date date = formatter.parse(taskInfo.getFinishTime());
			taskMonitorInfo.setFinishTime(formatter.format(date));
		}
		
		taskMonitorInfo.setDescription(taskInfo.getDescription());
		taskMonitorInfo.setException(taskInfo.getException());
		
		taskMonitorInfo.setRunStatus(taskInfo.getRunStatus());
		taskMonitorInfo.setVersion(taskInfo.getVersion());
		
		taskMonitorInfo.setDataFrom(taskInfo.getDataFrom());
		taskMonitorInfo.setDataQuantity(taskInfo.getDataQuantity());
		taskMonitorInfo.setDataRow(taskInfo.getDataRow());
		taskMonitorInfo.setDataTo(taskInfo.getDataTo());
		
		if(StringUtils.isNotEmpty(taskInfo.getCreateTime())){
			Date date = formatter.parse(taskInfo.getCreateTime());
			taskMonitorInfo.setCreateTime(formatter.format(date));
		}
		if(StringUtils.isNotEmpty(taskInfo.getLastModifiedTime())){
			Date date = formatter.parse(taskInfo.getLastModifiedTime());
			taskMonitorInfo.setLastModifiedTime(formatter.format(date));
		}
		
		taskMonitorInfo.setRejectedRow(taskInfo.getRejectedRow());
		taskMonitorInfo.setTimeout(taskInfo.getIsTimeout());
		taskMonitorInfo.setNode(taskInfo.getNode());
		
		taskMonitorInfo.setJobName(taskInfo.getJobName());
		taskMonitorInfo.setExecName(taskInfo.getExecName());
		taskMonitorInfo.setExecStrategy(taskInfo.getExecStrategy());
		taskMonitorInfo.setStartMode(taskInfo.getStartMode());
		
		return taskMonitorInfo;
	}
}
