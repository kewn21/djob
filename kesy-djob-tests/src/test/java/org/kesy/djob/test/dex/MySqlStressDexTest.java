/**
 * 2014年9月9日
 */
package org.kesy.djob.test.dex;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.kesy.djob.dex.DataexFactory;
import org.kesy.djob.dex.DataexParamFactory;
import org.kesy.djob.dex.engine.DataEngineListener;
import org.kesy.djob.dex.engine.DataEngineResult;
import org.kesy.djob.dex.engine.DataTaskParam;
import org.kesy.djob.dex.param.PluginName;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author kewn
 *
 */
public class MySqlStressDexTest {

	@Test
	public void test01() {
		StressDexTest01 test = new StressDexTest01(20);
		test.test();
		test.isFinished();
		
		System.out.println("StressDexTest01 finished");
	}
	
	@Test
	public void test02() {
		StressDexTest02 test = new StressDexTest02(20);
		test.test();
		test.isFinished();
		
		System.out.println("StressDexTest02 finished");
	}
	
	
	public static final class StressDexTest01 {
		private final AtomicInteger cnt = new AtomicInteger(0);
		private final int number;
		private long startTime;
		private long finishTime;
		
		public StressDexTest01(int number){
			this.number = number;
		}
		
		public void test() {
			startTime = System.currentTimeMillis();
			System.out.println("startTime: " + startTime);
			
			for (int i = 0; i < number; i++) {
				new StressDexThread().start();
			}
		}
		
		public boolean isFinished() {
			while (cnt.get() < number) {
			}
			
			finishTime = System.currentTimeMillis();
			System.out.println("finishTime: " + finishTime);
			System.out.println("totalCostTime: " + (finishTime - startTime));
			
			return true;
		}
		
		public final class StressDexThread extends Thread {

			@Override
			public void run() {
				//long startTime = System.currentTimeMillis();
				//System.out.println("startTime: " + startTime);
				
				String sSql = "SELECT dtStatDate, server, account, testId, channelId, channelName, dtDetail FROM tb_oss_download_log"; 
				
				//Class.forName("com.mysql.jdbc.Driver");
				BasicDataSource dataSource = new BasicDataSource();
				dataSource.setDriverClassName("com.mysql.jdbc.Driver");
				//dataSource.setUrl("jdbc:mysql://115.238.171.226:3306/dc_oss?characterEncoding=UTF-8");
				dataSource.setUrl("jdbc:mysql://115.238.171.226:3306/dc_oss?characterEncoding=UTF-8&rewriteBatchedStatements=true&useServerPrepStmts=false");
				dataSource.setUsername("root");
				dataSource.setPassword("thriftdy@819");
				
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				final List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sSql);
				
				//long startWriteTime = System.currentTimeMillis();
				
				String tSql = "REPLACE INTO tb_oss_download_log_t1 (dtStatDate, server, account, testId, channelName, dtDetail) VALUES (?, ?, ?, ?, ?, NOW())";
				int[] affectedRows = jdbcTemplate.batchUpdate(tSql, new BatchPreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setString(1, MapUtils.getString(dataList.get(i), "dtStatDate"));
						ps.setString(2, "-1");
						ps.setString(3, MapUtils.getString(dataList.get(i), "account"));
						ps.setString(4, "2");
						ps.setString(5, MapUtils.getString(dataList.get(i), "channelName"));
					}
					
					@Override
					public int getBatchSize() {
						return dataList.size();
					}
				});
				
				if (affectedRows != null && affectedRows.length > 0) {
					int totalRows = 0;
					for (int i : affectedRows) {
						totalRows += i;
					}
					if (totalRows != dataList.size()) {
						System.err.println("having some rows failed when batchUpdate to [tb_oss_download_log]");
					}
				}
				
				//long finishTime = System.currentTimeMillis();
				//System.out.println("finishTime: " + finishTime);
				
				//System.out.println("writeCostTime: " + (finishTime - startWriteTime));
				//System.out.println("totalCostTime: " + (finishTime - startTime));
				
				cnt.incrementAndGet();
			}
			
		}
	}
	
	
	public static final class StressDexTest02 {
		private final AtomicInteger cnt = new AtomicInteger(0);
		private final int number;
		private long startTime;
		private long finishTime;
		
		public StressDexTest02(int number){
			this.number = number;
		}
		
		public void test() {
			startTime = System.currentTimeMillis();
			System.out.println("startTime: " + startTime);
			
			for (int i = 0; i < number; i++) {
				new StressDexThread().start();
			}
		}
		
		public boolean isFinished() {
			while (cnt.get() < number) {
			}
			
			finishTime = System.currentTimeMillis();
			System.out.println("finishTime: " + finishTime);
			System.out.println("totalCostTime: " + (finishTime - startTime));
			
			return true;
		}
		
		public final class StressDexThread extends Thread {

			@Override
			public void run() {
				//final long startTime = System.currentTimeMillis();
				//System.out.println("startTime: " + startTime);
				
				String sSql = "SELECT dtStatDate, server, account, testId, channelId, channelName, dtDetail FROM tb_oss_download_log"; 
				
				Map<String, String> sparaMap = new HashMap<String, String>();
				sparaMap.put("ip", "115.238.171.226");
				sparaMap.put("username", "root");
				sparaMap.put("password", "thriftdy@819");
				sparaMap.put("dbname", "dc_oss");
				sparaMap.put("sql", sSql);
				
				Map<String, String> tparaMap = new HashMap<String, String>();
				tparaMap.put("ip", "115.238.171.226");
				tparaMap.put("username", "root");
				tparaMap.put("password", "thriftdy@819");
				tparaMap.put("dbname", "dc_oss");
				tparaMap.put("replace", "true");
				tparaMap.put("table", "tb_oss_download_log_t");
				
				DataTaskParam dataParam = DataexParamFactory.getParam(PluginName.MYSQL_READER, sparaMap
						, PluginName.MYSQL_WRITER, tparaMap);
				
				try {
					DataexFactory.getEngine()
					.run(dataParam, new DataEngineListener() {
						@Override
						public void notify(final DataEngineResult loadResult) {
							if (loadResult != null) {
								System.out.println("finsh to load data, data row : " 
										+ loadResult.getTransferedLine() 
										+ " , data quantity : "
										+ loadResult.getByteCount());	
							}
							else {
								System.err.println("load data failed");
							}
							
							//long finishTime = System.currentTimeMillis();
							//System.out.println("finishTime: " + finishTime);
							
							//System.out.println("costTime: " + (finishTime - startTime));
							
							cnt.incrementAndGet();
						}
					});
				} catch (Exception e) {
				}
			}
			
		}
	}

}
