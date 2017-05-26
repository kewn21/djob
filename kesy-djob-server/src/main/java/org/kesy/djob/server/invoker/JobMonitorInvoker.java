/**
 * This file created at 2014-1-4.
 *
 */
package org.kesy.djob.server.invoker;

import java.util.List;

import org.kesy.djob.sdu.api.JobMonitorFactory;
import org.kesy.djob.sdu.api.model.JobInfoModels.IntegerType;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfoList;
import org.kesy.djob.sdu.api.model.JobInfoModels.StringType;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfoList;
import org.kesy.djob.sdu.api.model.JobMonitorServiceProto.GetTaskMonitorsParam;
import org.kesy.djob.sdu.api.model.JobMonitorServiceProto.JobMonitorService.BlockingInterface;
import org.kesy.djob.sdu.api.model.JobMonitorServiceProto.SearchJobMonitorsParam;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;


/**
 * <code>{@link JobMonitorInvoker}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class JobMonitorInvoker implements BlockingInterface {
	
	//private static final Logger logger = LoggerFactory.getLogger(JobMonitorInvoker.class);

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.model.JobMonitorServiceProto.JobMonitorService.BlockingInterface#searchJobMonitorCount(com.google.protobuf.RpcController, org.kesy.djob.sdu.model.JobInfoModels.StringType)
	 */
	@Override
	public IntegerType searchJobMonitorCount(RpcController controller,
			SearchJobMonitorsParam request) throws ServiceException {
		int count = JobMonitorFactory.get().searchJobMonitorCount(request.getJobName(), request.getRunStatus());
		return IntegerType.newBuilder().setValue(count).build();
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.model.JobMonitorServiceProto.JobMonitorService.BlockingInterface#searchJobMonitors(com.google.protobuf.RpcController, org.kesy.djob.sdu.model.JobMonitorServiceProto.SearchJobMonitorsParam)
	 */
	@Override
	public JobInfoList searchJobMonitors(RpcController controller,
			SearchJobMonitorsParam request) throws ServiceException {
		List<JobInfo> jobInfos = JobMonitorFactory.get()
				.searchJobMonitors(request.getJobName()
						, request.getRunStatus()
						, request.getPageIndex()
						, request.getPageSize());
		return JobInfoList.newBuilder().addAllJobInfo(jobInfos).build();
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.model.JobMonitorServiceProto.JobMonitorService.BlockingInterface#getTaskMonitorCount(com.google.protobuf.RpcController, org.kesy.djob.sdu.model.JobInfoModels.StringType)
	 */
	@Override
	public IntegerType getTaskMonitorCount(RpcController controller,
			StringType request) throws ServiceException {
		int count = JobMonitorFactory.get().getTaskMonitorCount(request.getValue());
		return IntegerType.newBuilder().setValue(count).build();
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.model.JobMonitorServiceProto.JobMonitorService.BlockingInterface#getTaskMonitors(com.google.protobuf.RpcController, org.kesy.djob.sdu.model.JobMonitorServiceProto.GetTaskMonitorsParam)
	 */
	@Override
	public TaskInfoList getTaskMonitors(RpcController controller,
			GetTaskMonitorsParam request) throws ServiceException {
		List<TaskInfo> taskInfos = JobMonitorFactory.get()
				.getTaskMonitors(request.getJobId()
						, request.getPageIndex()
						, request.getPageSize());
		return TaskInfoList.newBuilder().addAllTaskInfo(taskInfos).build();
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.model.JobMonitorServiceProto.JobMonitorService.BlockingInterface#searchJobTaskMonitorCount(com.google.protobuf.RpcController, org.kesy.djob.sdu.api.model.JobMonitorServiceProto.SearchJobMonitorsParam)
	 */
	@Override
	public IntegerType searchJobTaskMonitorCount(RpcController controller,
			SearchJobMonitorsParam request) throws ServiceException {
		int count = JobMonitorFactory.get().searchJobTaskMonitorCount(request.getJobName()
				, request.getRunStatus()
				, request.getStartTime()
				, request.getEndTime());
		return IntegerType.newBuilder().setValue(count).build();
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.model.JobMonitorServiceProto.JobMonitorService.BlockingInterface#searchJobTaskMonitors(com.google.protobuf.RpcController, org.kesy.djob.sdu.api.model.JobMonitorServiceProto.SearchJobMonitorsParam)
	 */
	@Override
	public TaskInfoList searchJobTaskMonitors(RpcController controller,
			SearchJobMonitorsParam request) throws ServiceException {
		List<TaskInfo> taskInfos = JobMonitorFactory.get().searchJobTaskMonitors(request.getJobName()
				, request.getRunStatus()
				, request.getStartTime()
				, request.getEndTime()
				, request.getPageIndex()
				, request.getPageSize());
		return TaskInfoList.newBuilder().addAllTaskInfo(taskInfos).build();
	}

}
