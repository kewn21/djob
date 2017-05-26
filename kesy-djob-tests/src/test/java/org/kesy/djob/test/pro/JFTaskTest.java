/**
 * 2014年9月3日
 */
package org.kesy.djob.test.pro;

import org.junit.Test;
import org.kesy.djob.client.cmd.JobCmdFactory;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage;
import org.kesy.djob.sdu.api.model.JobMessageModels.JobMessage.TaskMessage;

/**
 * @author kewn
 *
 */
public class JFTaskTest {
	
	@Test
	public void createPcgameClickTest() {
		
		JobMessage.Builder jobBuilder = JobMessage.newBuilder();
		jobBuilder.setName("点击事件");
		jobBuilder.setExecStrategy("0 0/30 * * * ?");
		
		TaskMessage.Builder taskBuilder = TaskMessage.newBuilder();
		taskBuilder.setExecName("PcgameClick");
		jobBuilder.setTaskMessage(taskBuilder);
		
		JobCmdFactory.createJob(jobBuilder.build());
	}
	
	@Test
	public void createOriginPcgameEntryTest() {
		
		JobMessage.Builder jobBuilder = JobMessage.newBuilder();
		jobBuilder.setName("页面来源");
		jobBuilder.setExecStrategy("0 0/30 * * * ?");
		
		TaskMessage.Builder taskBuilder = TaskMessage.newBuilder();
		taskBuilder.setExecName("OriginPcgameEntry");
		jobBuilder.setTaskMessage(taskBuilder);
		
		JobCmdFactory.createJob(jobBuilder.build());
	}
	
	@Test
	public void createPopagesPcgameEntryTest() {
		
		JobMessage.Builder jobBuilder = JobMessage.newBuilder();
		jobBuilder.setName("最受欢迎页面");
		jobBuilder.setExecStrategy("0 0/30 * * * ?");
		
		TaskMessage.Builder taskBuilder = TaskMessage.newBuilder();
		taskBuilder.setExecName("PopagesPcgameEntry");
		jobBuilder.setTaskMessage(taskBuilder);
		
		JobCmdFactory.createJob(jobBuilder.build());
	}
	
	
	@Test
	public void createKeywordPcgameEntryTest() {
		
		JobMessage.Builder jobBuilder = JobMessage.newBuilder();
		jobBuilder.setName("关键字");
		jobBuilder.setExecStrategy("0 0/30 * * * ?");
		
		TaskMessage.Builder taskBuilder = TaskMessage.newBuilder();
		taskBuilder.setExecName("KeywordPcgameEntry");
		jobBuilder.setTaskMessage(taskBuilder);
		
		JobCmdFactory.createJob(jobBuilder.build());
	}
	
	@Test
	public void createSearchPcgameEntryTest() {
		
		JobMessage.Builder jobBuilder = JobMessage.newBuilder();
		jobBuilder.setName("搜索引擎");
		jobBuilder.setExecStrategy("0 0/30 * * * ?");
		
		TaskMessage.Builder taskBuilder = TaskMessage.newBuilder();
		taskBuilder.setExecName("SearchPcgameEntry");
		jobBuilder.setTaskMessage(taskBuilder);
		
		JobCmdFactory.createJob(jobBuilder.build());
	}
	
	@Test
	public void createDownloadLogTest() {
		
		JobMessage.Builder jobBuilder = JobMessage.newBuilder();
		jobBuilder.setName("下载日志");
		jobBuilder.setExecStrategy("1 1 13 * * ?");
		
		TaskMessage.Builder taskBuilder = TaskMessage.newBuilder();
		taskBuilder.setExecName("DownloadLog");
		jobBuilder.setTaskMessage(taskBuilder);
		
		JobCmdFactory.createJob(jobBuilder.build());
	}
	
	@Test
	public void createChannelDownloadTest() {
		
		JobMessage.Builder jobBuilder = JobMessage.newBuilder();
		jobBuilder.setName("渠道下载");
		jobBuilder.setExecStrategy("1 10 13 * * ?");
		
		TaskMessage.Builder taskBuilder = TaskMessage.newBuilder();
		taskBuilder.setExecName("ChannelDownload");
		jobBuilder.setTaskMessage(taskBuilder);
		
		JobCmdFactory.createJob(jobBuilder.build());
	}

}
