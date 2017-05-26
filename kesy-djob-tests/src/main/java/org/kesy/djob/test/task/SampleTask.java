package org.kesy.djob.test.task;

import org.apache.commons.lang.StringUtils;
import org.kesy.djob.core.json.Json;
import org.kesy.djob.sdu.api.TaskContext;
import org.kesy.djob.sdu.api.task.AbstractTaskExecutor;
import org.kesy.djob.sdu.api.task.TaskCallback;
import org.kesy.djob.sdu.api.task.TaskResult;
import org.kesy.djob.sdu.api.task.TaskRunParam;
import org.kesy.djob.sdu.api.task.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleTask extends AbstractTaskExecutor {
	
	private static final Logger logger = LoggerFactory.getLogger(SampleTask.class);
	
	private static final String			ALREADY_LOADED_DATA				= "已经装载完数据";

	@Override
	public TaskStatus init(String execParam) throws Exception {
		SampleExecParam param = Json.toObject(execParam, SampleExecParam.class);
		
		if (param != null && StringUtils.isNotEmpty(param.getContent())) {
			logger.info("init SampleTask [{}]", param.getContent());
		}
		else {
			logger.info("init SampleTask");
		}
		
		return TaskStatus.createBuilder().build();
	}

	@Override
	public void execute(TaskCallback callback) throws Exception {
		
		logger.info("executting task [{}] of job [{}]", taskId, jobId);
		
		System.out.println("SampleTask ScheduleDate : " + TaskContext.getScheduleDate().get());
		
		//for test
		Thread.sleep(10000);
		
		TaskResult result = TaskResult.createbBuilder()
				.setDataFrom(201408)
				.setDataTo(201409)
				.setDataRow(100)
				.setDataQuantity(1000)
				.setDecription(ALREADY_LOADED_DATA).build();
		
		callback.invoke(result);
	}

	@Override
	public void run(TaskRunParam runParam, TaskCallback callback)
			throws Exception {
		
		logger.info("run task [{}] of job [{}], from [{}] to [{}] with interval [{}]", taskId, jobId, runParam.getDataFrom(), runParam.getDataTo(), runParam.getInterval());
		
		//for test
		Thread.sleep(30000);
		
		TaskResult result = TaskResult.createbBuilder()
				.setDataFrom(runParam.getDataFrom())
				.setDataTo(runParam.getDataTo())
				.setDataRow(100)
				.setDataQuantity(1000)
				.setDecription(ALREADY_LOADED_DATA).build();
		
		callback.invoke(result);
	}
	
	
	public static final class SampleExecParam {
		
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
		public SampleExecParam() {
		}
		
		public SampleExecParam(String content) {
			this.content = content;
		}
	}

}
