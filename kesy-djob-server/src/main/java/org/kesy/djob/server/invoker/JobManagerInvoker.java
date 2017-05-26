/**
 * This file created at 2014-1-4.
 *
 */
package org.kesy.djob.server.invoker;


import java.util.List;

import org.kesy.djob.sdu.api.JobManagerFactory;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfoList;
import org.kesy.djob.sdu.api.model.JobInfoModels.StringList;
import org.kesy.djob.sdu.api.model.JobInfoModels.StringType;
import org.kesy.djob.sdu.api.model.JobInfoModels.VoidType;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.JobManagerResult;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.JobManagerService.BlockingInterface;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.RunJobParam;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.RunJobsParam;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage;
import org.kesy.djob.sdu.api.task.TaskRunParam;

import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;


/**
 * <code>{@link JobManagerInvoker}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class JobManagerInvoker implements BlockingInterface {
	
	@Override
	public JobManagerResult createJob(RpcController controller, JobMessage request)
			throws ServiceException {
		return JobManagerResult.newBuilder().setIsSuccessed(
				JobManagerFactory.get().createJob(request)).build();
	}

	@Override
	public JobManagerResult dropAll(RpcController controller, VoidType request)
			throws ServiceException {
		return JobManagerResult.newBuilder().setIsSuccessed(
				JobManagerFactory.get().dropAll()).build();
	}
	
    @Override
	public JobManagerResult dropJob(RpcController controller, StringType request)
			throws ServiceException {
		return JobManagerResult.newBuilder().setIsSuccessed(
				JobManagerFactory.get().dropJob(request.getValue())).build();
	}

    @Override
	public JobManagerResult dropJobs(RpcController controller, StringList request)
			throws ServiceException {
		int size = request.getValueList().size();
		String[] arr = (String[])request.getValueList().toArray(new String[size]);
		
		return JobManagerResult.newBuilder().setIsSuccessed(
				JobManagerFactory.get().dropJobs(arr)).build();
	}

	@Override
	public JobInfo findJob(RpcController controller, StringType request)
			throws ServiceException {
		return JobManagerFactory.get().findJob(request.getValue());
	}

	@Override
	public JobInfoList listJobs(RpcController controller, VoidType request)
			throws ServiceException {
		List<JobInfo> jobInfos = JobManagerFactory.get().listJobs();
		return JobInfoList.newBuilder().addAllJobInfo(jobInfos).build();
	}

	@Override
	public JobManagerResult restartAll(RpcController controller, VoidType request)
			throws ServiceException {
		return JobManagerResult.newBuilder().setIsSuccessed(
				JobManagerFactory.get().restartAll()).build();
	}

	@Override
	public JobManagerResult restartJob(RpcController controller, StringType request)
			throws ServiceException {
		return JobManagerResult.newBuilder().setIsSuccessed(
				JobManagerFactory.get().restartJob(request.getValue())).build();
	}

	@Override
	public JobManagerResult restartJobs(RpcController controller, StringList request)
			throws ServiceException {
		int size = request.getValueList().size();
		String[] arr = (String[])request.getValueList().toArray(new String[size]);
		
		return JobManagerResult.newBuilder().setIsSuccessed(
				JobManagerFactory.get().restartJobs(arr)).build();
	}


	@Override
	public JobManagerResult runJob(RpcController controller, RunJobParam request)
			throws ServiceException {
		return JobManagerResult.newBuilder().setIsSuccessed(JobManagerFactory.get().runJob(request.getJobId()
    			, new TaskRunParam(request.getDataFrom(), request.getDataTo(), request.getInterval()))).build();
	}
	
	@Override
	public JobManagerResult runJobs(RpcController controller, RunJobsParam request)
			throws ServiceException {
		int size = request.getJobIdList().size();
		String[] arr = (String[])request.getJobIdList().toArray(new String[size]);
		
		return JobManagerResult.newBuilder().setIsSuccessed(JobManagerFactory.get().runJobs(arr
    			, new TaskRunParam(request.getDataFrom(), request.getDataTo(), request.getInterval()))).build();
	}

	@Override
	public JobManagerResult shutdownAll(RpcController controller, VoidType request)
			throws ServiceException {
		return JobManagerResult.newBuilder()
				.setIsSuccessed(JobManagerFactory.get().shutdownAll()).build();
	}

	@Override
	public JobManagerResult shutdownJob(RpcController controller, StringType request)
			throws ServiceException {
		return JobManagerResult.newBuilder()
				.setIsSuccessed(JobManagerFactory.get().shutdownJob(request.getValue())).build();
	}

	@Override
	public JobManagerResult shutdownJobs(RpcController controller, StringList request)
			throws ServiceException {
		int size = request.getValueList().size();
		String[] arr = (String[])request.getValueList().toArray(new String[size]);
		
		return JobManagerResult.newBuilder()
				.setIsSuccessed(JobManagerFactory.get().shutdownJobs(arr)).build();
	}

	@Override
	public JobManagerResult startAll(RpcController controller, VoidType request)
			throws ServiceException {
		return JobManagerResult.newBuilder()
				.setIsSuccessed(JobManagerFactory.get().startAll()).build();
	}

	@Override
	public JobManagerResult startJob(RpcController controller, StringType request)
			throws ServiceException {
		return JobManagerResult.newBuilder()
				.setIsSuccessed(JobManagerFactory.get().startJob(request.getValue())).build();
	}

	@Override
	public JobManagerResult startJobs(RpcController controller, StringList request)
			throws ServiceException {
		int size = request.getValueList().size();
		String[] arr = (String[])request.getValueList().toArray(new String[size]);
		
		return JobManagerResult.newBuilder()
				.setIsSuccessed(
						JobManagerFactory.get().startJobs(arr)).build();
	}

	@Override
	public JobManagerResult updataJob(RpcController controller, JobMessage request)
			throws ServiceException {
		return JobManagerResult.newBuilder()
				.setIsSuccessed(JobManagerFactory.get().updateJob(request)).build();
	}

}
