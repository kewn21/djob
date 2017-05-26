/**
 * This file created at 2014-1-7.
 *
 */
package org.kesy.djob.store;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.kesy.djob.core.sql.SqlConfigFactory;
import org.kesy.djob.sdu.api.JobStore;
import org.kesy.djob.sdu.api.model.JobInfoModels.JobInfo;
import org.kesy.djob.sdu.api.model.JobInfoModels.TaskInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * <code>{@link JdbcJobStore}</code>
 *
 *
 * @author kewn
 */
public class JdbcJobStore implements JobStore {
	
	private JdbcTemplate dao;
	public void setDao(JdbcTemplate dao) {
		this.dao = dao;
	}

	private static final Logger logger = LoggerFactory.getLogger(JdbcJobStore.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final String				INSERT_JOB							= "job.insertJob";							// 插入JobInfo
	private static final String				SELECT_ALL_JOB						= "job.selectAllJob";						// 查找所有的JobInfo
	private static final String				SELECT_JOB							= "job.selectJob";							// 按名字查找到JobInfo信息
	private static final String				UPDATA_JOB							= "job.updateJob";							// 更新JobInfo
	private static final String				UPDATA_JOB_STATU					= "job.updateJobStatus";					// 更新Job状态信息
	private static final String				DELETE_ALL_JOB						= "job.dropAllJob";						// 删除所有的Job信息
	private static final String				DELETE_JOB							= "job.dropJob";							// 按照名字删除Job
	private static final String				UPDATA_JOB_MODE						= "job.updateJobMode";						// 更新Job启动方式
	private static final String				UPDATA_JOB_STATU_TO_WAITING			= "job.updateJobStatusToWaiting";			// 更新Job状态信息
	private static final String				UPDATA_JOB_STATU_NO_RUNNED_TASK		= "job.updateJobStatusWhenNoRunnedTask";	// 更新Job状态信息
	private static final String				UPDATA_RUNNED_TASK_STATUS_TO_ITER	= "job.updateRunnedTaskStatusToInterrupt";	// 更新task状态信息

	private static final String				INSERT_TASK							= "job.insertTask";						// 插入TaskInfo
	private static final String				UPDATA_TASK							= "job.updateTask";						// 更新Task信息
	private static final String				DELETE_ALL_TASK						= "job.dropAllTask";						// 删除所有的Task信息
	private static final String				DELETE_TASK_BY_JOB_ID				= "job.dropTaskByJobId";					// 按照名字删除Task信息
	private static final String				SELECT_TASK							= "job.selectTask";						// 根据jobId查找Task信息
	private static final String				SELECT_TASK_BY_JOB_ID				= "job.selectTaskByJobId";					// 根据jobId查找Task信息
	private static final String				UPDATA_TASK_STATU					= "job.updateTaskStatus";					// 更新task状态信息

	private static final String				SEARCH_JOB_COUNT					= "job.searchJobCount";					// 查询job总行数
	private static final String				GET_TASK_COUNT						= "job.getTaskCount";						// 查询task总行数

	private static final String				SEARCH_JOB_TASK_COUNT				= "job.searchJobTaskCount";				// 查询job
																															// task总行数
	private static final String				SEARCH_JOB_TASK						= "job.searchJobTask";						// 查询job
																															// task

	private static final String				JOB_ID								= "id";
	private static final String				JOB_NAME							= "name";
	private static final String				DESCRIPTION							= "description";
	private static final String				EXCEPTION							= "exception";
	private static final String				EXEC_PARAM							= "exec_param";
	private static final String				EXEC_NAME							= "exec_name";
	private static final String				EXEC_STRATEGY						= "exec_strategy";
	private static final String				RUN_STATUS							= "run_status";
	private static final String				START_MODE							= "start_mode";
	private static final String				LABEL								= "label";
	private static final String				TIMEOUT								= "timeout";

	private static final String				TASK_ID								= "id";
	private static final String				TASK_JOB_ID							= "job_id";
	private static final String				START_TIME							= "start_time";
	private static final String				FINISH_TIME							= "finish_time";
	private static final String				CREATOR								= "creator";
	private static final String				CREATE_TIME							= "create_time";
	private static final String				LAST_MODIFIED_TIME					= "last_modified_time";

	private static final String				VERSION								= "version";
	private static final String				DATA_FROM							= "data_from";
	private static final String				DATA_TO								= "data_to";
	private static final String				DATA_ROW							= "data_row";
	private static final String				DATA_QUANTITY						= "data_quantity";

	private static final String				REJECTED_ROW						= "rejected_row";
	private static final String				IS_TIMEOUT							= "is_timeout";
	private static final String				NODE								= "node";

	
	/**
	 * job操作
	 */
	
	@Override
	public boolean deleteAll() {
		String deleteTaskSql = SqlConfigFactory.getSql(DELETE_ALL_TASK);
		if (dao.update(deleteTaskSql) < 0) {
			logger.error("Delete TaskInfo of all jobs failed.");
			return false;
		}
		
		String deleteJobSql = SqlConfigFactory.getSql(DELETE_ALL_JOB);
		if (dao.update(deleteJobSql) < 0) {
			logger.error("Delete all JobInfo failed.");
			return false;
		}
		
		return true;
	}

	@Override
	public boolean deleteJob(final JobInfo jobInfo) {
		String deleteTaskSql = SqlConfigFactory.getSql(DELETE_TASK_BY_JOB_ID);
		if (dao.update(deleteTaskSql, new Object[] { jobInfo.getId() }) < 0) {
			logger.error("Delete TaskInfo of job [{}] failed.", jobInfo.getId());
			return false;
		}
		
		String deleteJobSql = SqlConfigFactory.getSql(DELETE_JOB);
		if (dao.update(deleteJobSql, new Object[] { jobInfo.getId() }) < 0) {
			logger.error("Delete JobInfo [{}] failed.", jobInfo.getId());
			return false;
		}
		
		return true;
	}

	@Override
	public boolean deleteJobs(final String[] jobIds) {
		String deleteTaskSql = SqlConfigFactory.getSql(DELETE_TASK_BY_JOB_ID);
		int[] affectedRows = dao.batchUpdate(deleteTaskSql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, jobIds[i]);
			}

			@Override
			public int getBatchSize() {
				return jobIds.length;
			}
		});
		
		for (int i = 0; i < affectedRows.length; i++) {
			if (affectedRows[i] < 0) {
				logger.error("Batch delete TaskInfo failed.");
				return false;
			}
		}

		String deleteJobSql = SqlConfigFactory.getSql(DELETE_JOB);
		affectedRows = dao.batchUpdate(deleteJobSql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, jobIds[i]);
			}

			@Override
			public int getBatchSize() {
				return jobIds.length;
			}
		});

		for (int i = 0; i < affectedRows.length; i++) {
			if (affectedRows[i] < 0) {
				logger.error("Batch delete JobInfo failed.");
				return false;
			}
		}
		
		return true;
	}

	@Override
	public JobInfo insertJob(JobInfo jobInfo) {
		if (StringUtils.isEmpty(jobInfo.getId())) {
			jobInfo = JobInfo.newBuilder(jobInfo)
					.setId(UUID.randomUUID().toString())
					.build();
			return insertJobFinal(jobInfo);
		} else {
			return insertJobFinal(jobInfo);
		}
	}

	private JobInfo insertJobFinal(final JobInfo jobInfo) {
		String sql = SqlConfigFactory.getSql(INSERT_JOB);
		int affectedRow = dao.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, jobInfo.getId());
				ps.setString(2, jobInfo.getName());
				ps.setString(3, jobInfo.getStartMode());
				ps.setString(4, jobInfo.getRunStatus());
				ps.setString(5, jobInfo.getDescription());
				ps.setString(6, jobInfo.getExecStrategy());
				ps.setString(7, jobInfo.getExecName());
				ps.setString(8, jobInfo.getExecParam());
				ps.setLong(9, jobInfo.getTimeout());
				ps.setString(10, jobInfo.getLabel());

				ps.setString(11, jobInfo.getCreator());
				ps.setString(12, jobInfo.getCreator());
				ps.setString(13, dateFormat.format(new Date()));
				ps.setString(14, dateFormat.format(new Date()));
			}
		});
		
		if (affectedRow > 0) {
			//logger.debug("Insert JobInfo successfully.");
			return jobInfo;
		}
		else {
			logger.debug("Insert JobInfo [{}] failed.", jobInfo.getId());
			return null;
		}
	}

	@Override
	public boolean updateJob(final JobInfo jobInfo) {
		String sql = SqlConfigFactory.getSql(UPDATA_JOB);
		int affectedRow = dao.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, jobInfo.getName());
				ps.setString(2, jobInfo.getStartMode());
				ps.setString(3, jobInfo.getRunStatus());
				ps.setString(4, jobInfo.getDescription());
				ps.setString(5, jobInfo.getException());
				ps.setString(6, jobInfo.getExecStrategy());
				ps.setString(7, jobInfo.getExecName());
				ps.setString(8, jobInfo.getExecParam());
				ps.setLong(9, jobInfo.getTimeout());
				ps.setString(10, jobInfo.getLabel());
				ps.setString(11, dateFormat.format(new Date()));
				ps.setString(12, jobInfo.getId());
			}
		});
		
		if (affectedRow > -1) {
			//logger.debug("Update JobInfo successfully.");
			return true;
		} else {
			logger.error("Update JobInfo [{}] failed.", jobInfo.getId());
			return false;
		}
	}

	@Override
	public boolean updateJobStatus(final String jobId, final String status) {
		String sql = SqlConfigFactory.getSql(UPDATA_JOB_STATU);
		int affectedRow = dao.update(sql
				, new Object[] { status
								, dateFormat.format(new Date())
								, jobId});
		
		if (affectedRow > -1) {
			//logger.debug("Update job status successfully.");
			return true;
		} else {
			logger.error("Update job [{}] status failed.", jobId);
			return false;
		}
	}

	@Override
	public boolean updateJobMode(final String jobId, final String mode) {
		String sql = SqlConfigFactory.getSql(UPDATA_JOB_MODE);
		int affectedRow = dao.update(sql
				, new Object[] { mode
								, dateFormat.format(new Date())
								, jobId});
		
		if (affectedRow > -1) {
			//logger.debug("Update job mode successfully.");
			return true;
		} else {
			logger.error("Update job [{}] mode failed.", jobId);
			return false;
		}
	}

	@Override
	public boolean updateJobStatusWhenNoRunningTask(final String jobId) {
		String sql = SqlConfigFactory.getSql(UPDATA_JOB_STATU_NO_RUNNED_TASK);
		int affectedRow = dao.update(sql
				, new Object[] { dateFormat.format(new Date())
								, jobId});
		
		if (affectedRow > -1) {
			//logger.debug("Update job status successfully.");
			return true;
		} else {
			logger.error("Update job [{}] status failed.", jobId);
			return false;
		}
	}

	@Override
	public boolean updateJobStatusToWaiting(final String jobId) {
		String sql = SqlConfigFactory.getSql(UPDATA_JOB_STATU_TO_WAITING);
		int affectedRow = dao.update(sql
				, new Object[] { dateFormat.format(new Date())
								, jobId});
		
		if (affectedRow > -1) {
			//logger.debug("Update job status successfully.");
			return true;
		} else {
			logger.error("Update job [{}] status failed.", jobId);
			return false;
		}
	}

	@Override
	public boolean updateRunningTaskStatusToInterrupt(final String jobId) {
		String sql = SqlConfigFactory.getSql(UPDATA_RUNNED_TASK_STATUS_TO_ITER);
		int affectedRow = dao.update(sql
				, new Object[] { dateFormat.format(new Date())
								, jobId});
		
		if (affectedRow > -1) {
			//logger.debug("Update task status successfully.");
			return true;
		} else {
			logger.error("Update job [{}] status failed.", jobId);
			return false;
		}
	}
	

	@Override
	public List<JobInfo> selectJobs() {
		String sql = SqlConfigFactory.getSql(SELECT_ALL_JOB);
		return dao.query(sql, new JobInfoResultSetExtractor());
	}

	@Override
	public JobInfo selectJob(final String jobId) {
		String sql = SqlConfigFactory.getSql(SELECT_JOB);
		List<JobInfo> jobInfos = dao.query(sql
				, new Object[] { jobId }
				, new JobInfoResultSetExtractor());
		
		if (jobInfos.size() > 0) {
			return jobInfos.get(0);
		}
		return null;
	}

	
	/**
	 * task操作
	 */

	@Override
	public TaskInfo insertTask(TaskInfo taskInfo, String jobId) {
		if (StringUtils.isEmpty(taskInfo.getId())) {
			return insertTaskInfo(TaskInfo.newBuilder(taskInfo)
					.setId(UUID.randomUUID().toString()).build()
					, jobId);
		} else {
			return insertTaskInfo(taskInfo, jobId);
		}
	}
	
	private TaskInfo insertTaskInfo(final TaskInfo taskInfo, final String jobId) {
		String sql = SqlConfigFactory.getSql(INSERT_TASK);
		
		int affectedRow = dao.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, taskInfo.getId());
				ps.setString(2, jobId);
				ps.setString(3, dateFormat.format(new Date()));
				ps.setString(4, taskInfo.getRunStatus());
				ps.setString(5, taskInfo.getDescription());
				ps.setString(6, taskInfo.getNode());
				ps.setString(7, dateFormat.format(new Date()));
				ps.setString(8, dateFormat.format(new Date()));
			}
		});
		
		if (affectedRow > 0) {
			//logger.debug("Insert TaskInfo successfully.");
			return taskInfo;
		}
		else {
			logger.error("Insert TaskInfo [{}] of job [{}] failed.", taskInfo.getId(), jobId);
			return null;
		}
	}

	@Override
	public boolean updateTask(final TaskInfo taskInfo) {
		String sql = SqlConfigFactory.getSql(UPDATA_TASK);
		int affectedRow = dao.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, dateFormat.format(new Date()));
				ps.setString(2, taskInfo.getRunStatus());
				ps.setString(3, taskInfo.getDescription());
				ps.setString(4, taskInfo.getException());
				ps.setLong(5, taskInfo.getVersion());
				ps.setLong(6, taskInfo.getDataFrom());
				ps.setLong(7, taskInfo.getDataTo());
				ps.setLong(8, taskInfo.getDataRow());
				ps.setDouble(9, taskInfo.getDataQuantity());
				ps.setLong(10, taskInfo.getRejectedRow());
				ps.setBoolean(11, taskInfo.getIsTimeout());
				ps.setString(12, dateFormat.format(new Date()));
				
				ps.setString(13, taskInfo.getId());
			}
		});
		
		if (affectedRow > -1) {
			//logger.debug("Update TaskInfo successfully.");
			return true;
		}
		else {
			logger.error("Update TaskInfo [{}] failed.", taskInfo.getId());
			return false;
		}
	}

	@Override
	public boolean updateTaskStatus(final String taskId, final String runStatus) {
		String sql = SqlConfigFactory.getSql(UPDATA_TASK_STATU);
		int affectedRow = dao.update(sql
				, new Object[] { runStatus
								, dateFormat.format(new Date())
								, taskId });
		
		if (affectedRow > -1) {
			//logger.debug("Update task status successfully.");
			return true;
		} else {
			logger.error("Update task [{}] status failed.", taskId);
			return false;
		}
	}
	
	@Override
	public List<TaskInfo> selectTasks(final String jobId) {
		String sql = SqlConfigFactory.getSql(SELECT_TASK_BY_JOB_ID);
		return dao.query(sql
				, new Object[] { jobId }
				, new TaskInfoResultSetExtractor());
	}

	@Override
	public TaskInfo selectTask(final String taskId) {
		String sql = SqlConfigFactory.getSql(SELECT_TASK);
		List<TaskInfo> taskInfos = dao.query(sql
				, new Object[] { taskId }
				, new TaskInfoResultSetExtractor());
		
		if (taskInfos.size() > 0) {
			return taskInfos.get(0);
		}
		return null;
	}
	
	
	private static final class JobInfoResultSetExtractor implements ResultSetExtractor<List<JobInfo>> {

		@Override
		public List<JobInfo> extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			 List<JobInfo> jobInfos = new ArrayList<JobInfo>();
			 while (rs.next()) {
				 JobInfo.Builder builder = JobInfo.newBuilder();

				builder.setId(rs.getString(JOB_ID));
				if (StringUtils.isNotEmpty(rs.getString(JOB_NAME))) {
					builder.setName(rs.getString(JOB_NAME));
				}
				if (StringUtils.isNotEmpty(rs.getString(START_MODE))){
					builder.setStartMode(rs.getString(START_MODE));
				}
				if (StringUtils.isNotEmpty(rs.getString(RUN_STATUS))) {
					builder.setRunStatus(rs.getString(RUN_STATUS));
				}
				if (StringUtils.isNotEmpty(rs.getString(DESCRIPTION))) {
					builder.setDescription(rs.getString(DESCRIPTION));
				}
				if (StringUtils.isNotEmpty(rs.getString(EXCEPTION))) {
					builder.setException(rs.getString(EXCEPTION));
				}
				if (StringUtils.isNotEmpty(rs.getString(EXEC_STRATEGY))) {
					builder.setExecStrategy(rs.getString(EXEC_STRATEGY));
				}
				if (StringUtils.isNotEmpty(rs.getString(EXEC_NAME))) {
					builder.setExecName(rs.getString(EXEC_NAME));
				}
				if (StringUtils.isNotEmpty(rs.getString(EXEC_PARAM))) {
					builder.setExecParam(rs.getString(EXEC_PARAM));
				}
				if (StringUtils.isNotEmpty(rs.getString(TIMEOUT))) {
					builder.setTimeout(rs.getLong(TIMEOUT));
				}
				if (StringUtils.isNotEmpty(rs.getString(LABEL))) {
					builder.setLabel(rs.getString(LABEL));
				}
				
				if (StringUtils.isNotEmpty(rs.getString(CREATOR))) {
					builder.setCreator(rs.getString(CREATOR));
				}
				if (StringUtils.isNotEmpty(rs.getString(CREATE_TIME))) {
					builder.setCreateTime(rs.getString(CREATE_TIME));
				}
				if (StringUtils.isNotEmpty(rs.getString(LAST_MODIFIED_TIME))) {
					builder.setLastModifiedTime(rs.getString(LAST_MODIFIED_TIME));
				}
				
				jobInfos.add(builder.build());
			 }
			 
			 return jobInfos;
		}
	} 
	
	private static final class TaskInfoResultSetExtractor implements ResultSetExtractor<List<TaskInfo>> {
		
		private boolean withJobInfo = false;
		
		private TaskInfoResultSetExtractor(){
			this(false);
		}
		
		private TaskInfoResultSetExtractor(boolean withJobInfo){
			this.withJobInfo = withJobInfo;
		}

		@Override
		public List<TaskInfo> extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
			while (rs.next()) {
				TaskInfo.Builder builder = TaskInfo.newBuilder();
				
				builder.setId(rs.getString(TASK_ID));
				builder.setJobId(rs.getString(TASK_JOB_ID));
				if (StringUtils.isNotEmpty(rs.getString(START_TIME))) {
					builder.setStartTime(rs.getString(START_TIME));
				}
				if (StringUtils.isNotEmpty(rs.getString(FINISH_TIME))) {
					builder.setFinishTime(rs.getString(FINISH_TIME));
				}
				if (StringUtils.isNotEmpty(rs.getString(RUN_STATUS))) {
					builder.setRunStatus(rs.getString(RUN_STATUS));
				}
				if (StringUtils.isNotEmpty(rs.getString(DESCRIPTION))) {
					builder.setDescription(rs.getString(DESCRIPTION));
				}
				if (StringUtils.isNotEmpty(rs.getString(EXCEPTION))) {
					builder.setException(rs.getString(EXCEPTION));
				}
				if(StringUtils.isNotEmpty(rs.getString(VERSION))){
				    builder.setVersion(rs.getLong(VERSION));
		        }
		        if(StringUtils.isNotEmpty(rs.getString(DATA_FROM))){
				    builder.setDataFrom(rs.getLong(DATA_FROM));
		        }
		        if(StringUtils.isNotEmpty(rs.getString(DATA_TO))){
			 	    builder.setDataTo(rs.getLong(DATA_TO));
		        }
		        if(StringUtils.isNotEmpty(rs.getString(DATA_ROW))){
				    builder.setDataRow(rs.getLong(DATA_ROW));
		        }
		        if(StringUtils.isNotEmpty(rs.getString(DATA_QUANTITY))){
				    builder.setDataQuantity(rs.getDouble(DATA_QUANTITY));
		        }
		        
		        if(StringUtils.isNotEmpty(rs.getString(REJECTED_ROW))){
				    builder.setRejectedRow(rs.getLong(REJECTED_ROW));
		        }
		        if(StringUtils.isNotEmpty(rs.getString(IS_TIMEOUT))){
				    builder.setIsTimeout(rs.getBoolean(IS_TIMEOUT));
		        }
		        if (StringUtils.isNotEmpty(rs.getString(NODE))) {
					builder.setNode(rs.getString(NODE));
				}
		        
		        if (StringUtils.isNotEmpty(rs.getString(CREATE_TIME))) {
					builder.setCreateTime(rs.getString(CREATE_TIME));
				}
				if (StringUtils.isNotEmpty(rs.getString(LAST_MODIFIED_TIME))) {
					builder.setLastModifiedTime(rs.getString(LAST_MODIFIED_TIME));
				}
				
				if (withJobInfo) {
					if (StringUtils.isNotEmpty(rs.getString(JOB_NAME))) {
						builder.setJobName(rs.getString(JOB_NAME));
					}
					if (StringUtils.isNotEmpty(rs.getString(START_MODE))) {
						builder.setStartMode(rs.getString(START_MODE));
					}
					if (StringUtils.isNotEmpty(rs.getString(EXEC_STRATEGY))) {
						builder.setExecStrategy(rs.getString(EXEC_STRATEGY));
					}
					if (StringUtils.isNotEmpty(rs.getString(EXEC_NAME))) {
						builder.setExecName(rs.getString(EXEC_NAME));
					}
				}
		        
		        taskInfos.add(builder.build());
			}
			
			return taskInfos;
		}
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobStore#searchJobInfoCount(java.lang.String, java.lang.String)
	 */
	@Override
	public int searchJobInfoCount(String jobName, String runStatus) {
		String sql = SqlConfigFactory.getSql(SEARCH_JOB_COUNT);
		StringBuilder sb = new StringBuilder(sql).append(" where 1 = 1");
		if (StringUtils.isNotEmpty(jobName)) {
			sb.append(" and ")
						.append("name like '%")
						.append(jobName)
						.append("%'");
		}
		if (StringUtils.isNotEmpty(runStatus)) {
			sb.append(" and ")
						.append("run_status like '%")
						.append(runStatus)
						.append("%'");
		}
		
		return dao.query(sb.toString(), new ResultSetExtractor<Integer>(){

			@Override
			public Integer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					Object obj = rs.getObject(1);
					return obj != null ? Integer.parseInt(obj.toString()) : 0;
				}
				return 0;
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobStore#searchJobInfos(java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public List<JobInfo> searchJobInfos(String jobName, String runStatus, int pageIndex,
			int pageSize) {
		String sql = SqlConfigFactory.getSql(SELECT_ALL_JOB);
		StringBuilder sb = new StringBuilder(sql).append(" where 1 = 1");
		if (StringUtils.isNotEmpty(jobName)) {
			sb.append(" and ")
						.append("name like '%")
						.append(jobName)
						.append("%'");
		}
		if (StringUtils.isNotEmpty(runStatus)) {
			sb.append(" and ")
						.append("run_status like '%")
						.append(runStatus)
						.append("%'");
		}
		
		sb.append(" order by last_modified_time desc");
		
		if (pageIndex != 0) {
			sb.append(" limit ")
						.append((pageIndex - 1) * pageSize).append(",")
						.append(pageSize);
		}
		
		return dao.query(sb.toString()
				, new JobInfoResultSetExtractor());
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobStore#getTaskInfoCount(java.lang.String)
	 */
	@Override
	public int getTaskInfoCount(String jobId) {
		String sql = SqlConfigFactory.getSql(GET_TASK_COUNT);
		return dao.query(sql
				, new Object[] { jobId }
				, new ResultSetExtractor<Integer>(){

			@Override
			public Integer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					Object obj = rs.getObject(1);
					return obj != null ? Integer.parseInt(obj.toString()) : 0;
				}
				return 0;
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.JobStore#getTaskInfos(java.lang.String, int, int)
	 */
	@Override
	public List<TaskInfo> getTaskInfos(String jobId, int pageIndex, int pageSize) {
		String sql = SqlConfigFactory.getSql(SELECT_TASK_BY_JOB_ID);
		StringBuilder sb = new StringBuilder(sql).append(" order by start_time desc");
		if (pageIndex != 0) {
			sb.append(" limit ")
						.append((pageIndex - 1) * pageSize).append(",")
						.append(pageSize);
		}
		
		return dao.query(sb.toString()
				, new Object[] { jobId }
				, new TaskInfoResultSetExtractor());
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.JobStore#searchJobTaskInfoCount(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int searchJobTaskInfoCount(String jobName, String runStatus,
			String startTime, String endTime) {
		String sql = SqlConfigFactory.getSql(SEARCH_JOB_TASK_COUNT);
		StringBuilder sb = new StringBuilder(sql).append(" where 1 = 1");
		if (StringUtils.isNotEmpty(jobName)) {
			sb.append(" and ")
						.append("j.name like '%")
						.append(jobName)
						.append("%'");
		}
		if (StringUtils.isNotEmpty(runStatus)) {
			sb.append(" and ")
						.append("t.run_status = '")
						.append(runStatus)
						.append("'");
		}
		if (StringUtils.isNotEmpty(startTime)) {
			sb.append(" and ")
						.append("DATE_FORMAT(t.finish_time,'%Y-%m-%d') >= '")
						.append(startTime)
						.append("'");
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sb.append(" and ")
						.append("DATE_FORMAT(t.finish_time,'%Y-%m-%d') <= '")
						.append(endTime)
						.append("'");
		}
		
		return dao.query(sb.toString(), new ResultSetExtractor<Integer>(){

			@Override
			public Integer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					Object obj = rs.getObject(1);
					return obj != null ? Integer.parseInt(obj.toString()) : 0;
				}
				return 0;
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.api.JobStore#searchJobTaskInfos(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public List<TaskInfo> searchJobTaskInfos(String jobName, String runStatus,
			String startTime, String endTime, int pageIndex, int pageSize) {
		String sql = SqlConfigFactory.getSql(SEARCH_JOB_TASK);
		StringBuilder sb = new StringBuilder(sql).append(" where 1 = 1");
		if (StringUtils.isNotEmpty(jobName)) {
			sb.append(" and ")
						.append("j.name like '%")
						.append(jobName)
						.append("%'");
		}
		if (StringUtils.isNotEmpty(runStatus)) {
			sb.append(" and ")
						.append("t.run_status = '")
						.append(runStatus)
						.append("'");
		}
		if (StringUtils.isNotEmpty(startTime)) {
			sb.append(" and ")
						.append("DATE_FORMAT(t.finish_time,'%Y-%m-%d') >= '")
						.append(startTime)
						.append("'");
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sb.append(" and ")
						.append("DATE_FORMAT(t.finish_time,'%Y-%m-%d') <= '")
						.append(endTime)
						.append("'");
		}
		
		sb.append(" order by t.finish_time desc");
		
		if (pageIndex != 0) {
			sb.append(" limit ")
						.append((pageIndex - 1) * pageSize).append(",")
						.append(pageSize);
		}
		
		return dao.query(sb.toString()
				, new TaskInfoResultSetExtractor(true));
	}
}
