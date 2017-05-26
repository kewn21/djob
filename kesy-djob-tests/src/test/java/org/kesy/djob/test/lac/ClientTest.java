/**
 * 2014年8月22日
 */
package org.kesy.djob.test.lac;

import org.junit.Test;
import org.kesy.djob.client.cmd.JobCmdFactory;
import org.kesy.djob.core.json.Json;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage.TaskMessage;
import org.kesy.djob.test.task.SampleTask.SampleExecParam;

/**
 * @author kewn
 *
 */
public class ClientTest {
	
	@Test
	public void createJobTest() {
		
		JobMessage.Builder jobBuilder = JobMessage.newBuilder();
		jobBuilder.setName("test");
		jobBuilder.setExecStrategy("0 0/5 * * * ?");
		
		SampleExecParam param = new SampleExecParam("test");
		TaskMessage.Builder taskBuilder = TaskMessage.newBuilder();
		taskBuilder.setExecName("task1");
		taskBuilder.setExecParam(Json.toJson(param));
		jobBuilder.setTaskMessage(taskBuilder);
		
		JobCmdFactory.createJob(jobBuilder.build());
	}

}
