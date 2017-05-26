/**
 * This file created at 2014-1-13.
 *
 */
package org.kesy.djob.client.caller;

import org.kesy.djob.client.JobClientFactory;
import org.kesy.djob.rpc.SocketRpcController;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfoList;
import org.kesy.djob.sdu.api.model.JobInfoModels.StringList;
import org.kesy.djob.sdu.api.model.JobInfoModels.StringType;
import org.kesy.djob.sdu.api.model.JobInfoModels.VoidType;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.JobManagerResult;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.JobManagerService;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.JobManagerService.BlockingInterface;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.RunJobParam;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.RunJobsParam;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage;

import com.google.protobuf.BlockingRpcChannel;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;




/**
 * <code>{@link JobManagerCaller}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class JobManagerCaller {
	
	private RpcController controller = new SocketRpcController();
	private BlockingInterface service;
	
    public JobManagerCaller(){
    	BlockingRpcChannel channel = JobClientFactory.getChannel();
    	service = JobManagerService.newBlockingStub(channel);
	}
    
	public JobManagerResult createJob(JobMessage request) throws ServiceException {
		return service.createJob(controller, request);//是否需要类对象的转换
	}

	public JobManagerResult dropAll() throws ServiceException {
		VoidType.Builder voidType = VoidType.newBuilder();
		return service.dropAll(controller, voidType.build());
	}

	public JobManagerResult dropJob(String jobId) throws ServiceException {
		StringType.Builder stringType  =  StringType.newBuilder(); //创建StringType
		stringType.setValue(jobId);
		return service.dropJob(controller, stringType.build());
	}

	public JobManagerResult dropJobs(String[] jobIds) throws ServiceException {
		StringList.Builder strings  =  StringList.newBuilder(); //创建StringType
		for(int i = 0; i < jobIds.length; i++){
			strings.addValue(jobIds[i]);
		}
		return service.dropJobs(controller, strings.build());
	}

	public JobInfo findJob(String jobId) throws ServiceException {
		StringType.Builder stringType  =  StringType.newBuilder(); //创建StringType
		stringType.setValue(jobId);
		return service.findJob(controller, stringType.build());
	}

	public JobInfoList listJobs() throws ServiceException {
		VoidType.Builder voidType = VoidType.newBuilder(); 
		return service.listJobs(controller, voidType.build());
	}

	public JobManagerResult restartAll() throws ServiceException {
		VoidType.Builder voidType = VoidType.newBuilder(); 
		return service.restartAll(controller, voidType.build());
	}

	public JobManagerResult restartJob(String jobId) throws ServiceException {
		StringType.Builder stringType  =  StringType.newBuilder(); //创建StringType
		stringType.setValue(jobId);
		return service.restartJob(controller, stringType.build());
	}

	public JobManagerResult restartJobs(String[] jobIds) throws ServiceException {
		StringList.Builder strings  =  StringList.newBuilder(); //创建StringType
		for(int i = 0; i < jobIds.length; i++){
			strings.addValue(jobIds[i]);
		}
		return service.restartJobs(controller, strings.build());
	}

	public JobManagerResult runJob(String jobId, long dataFrom, long dataTo, int interval) throws ServiceException {
		return service.runJob(controller, RunJobParam.newBuilder().setJobId(jobId)
				.setDataFrom(dataFrom).setDataTo(dataTo).setInterval(interval).build());
	}
	
	public JobManagerResult runJobs(String[] jobIds, long dataFrom, long dataTo, int interval) throws ServiceException {
		RunJobsParam.Builder builder = RunJobsParam.newBuilder().setDataFrom(dataFrom).setDataTo(dataTo).setInterval(interval);
		for(int i = 0; i < jobIds.length; i++){
			builder.addJobId(jobIds[i]);
		}
		
		return service.runJobs(controller, builder.build());
	}

	public JobManagerResult shutdownAll() throws ServiceException {
		VoidType.Builder vtypeType = VoidType.newBuilder();
		return service.shutdownAll(controller, vtypeType.build());
	}

	public JobManagerResult shutdownJob(String jobId) throws ServiceException {
		StringType.Builder stringType  =  StringType.newBuilder(); //创建StringType
		stringType.setValue(jobId);
		return service.shutdownJob(controller, stringType.build());
	}

	public JobManagerResult shutdownJobs(String[] jobIds) throws ServiceException {
		StringList.Builder strings  =  StringList.newBuilder(); //创建StringType
		for(int i = 0; i < jobIds.length; i++){
			strings.addValue(jobIds[i]);
		}
		return service.shutdownJobs(controller, strings.build());
	}

	public JobManagerResult startAll() throws ServiceException {
		VoidType.Builder vtypeType = VoidType.newBuilder();
		return service.startAll(controller, vtypeType.build());
	}

	public JobManagerResult startJob(String jobId) throws ServiceException {
		StringType.Builder stringType  =  StringType.newBuilder(); //创建StringType
		stringType.setValue(jobId);
		return service.startJob(controller, stringType.build());
	}
	
	public JobManagerResult startJobs(String[] jobIds) throws ServiceException {
		StringList.Builder strings  =  StringList.newBuilder(); //创建StringType
		for(int i = 0; i < jobIds.length; i++){
			strings.addValue(jobIds[i]);
		}
		return service.startJobs(controller, strings.build());
	}

	public JobManagerResult updateJob(JobMessage request) throws ServiceException {
		return service.updataJob(controller, request);
	}

}
