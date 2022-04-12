/**
 * @date 2014年8月20日
 */
package org.kesy.djob.test.dex;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 */
public class MySqlDexTest {
	
	@Test
	public void test01() throws Exception {
		
		String sSql = "SELECT DISTINCT DATE_FORMAT(lr.tReportTime, '%Y-%m-%d'), '-1', l.iUdbUid, '2', lr.vChannel  "
				+ "FROM tbloginreportlog2005 lr JOIN tbloginlog2005 l  "
				+ "ON lr.iGameId = l.iGameId and lr.iYyuid = l.iYyuid "
				+ "and date_format(lr.tReportTime, '%Y-%m-%d') = date_format(l.tLogTime, '%Y-%m-%d') "
				+ "WHERE lr.iResult = 0 and lr.iGameId = '2005' "
				+ "and lr.vChannel IS NOT NULL AND TRIM(lr.vChannel) != '' "
				+ "and date_format(lr.tReportTime, '%Y-%m-%d') = '2014-08-12'"; 
		
		//源的信息
		Map<String, String> sparaMap = new HashMap<String, String>();
		sparaMap.put("ip", "localhost");
		sparaMap.put("username", "root");
		sparaMap.put("password", "123456");
		sparaMap.put("dbname", "dyloginsl");
		sparaMap.put("sql", sSql);
		
		//目标的信息
		Map<String, String> tparaMap = new HashMap<String, String>();
		tparaMap.put("ip", "localhost");
		tparaMap.put("username", "root");
		tparaMap.put("password", "123456");
		tparaMap.put("dbname", "dc_oss");
		tparaMap.put("table", "tb_oss_download_log_t");
		tparaMap.put("replace", "true");
		tparaMap.put("colorder", "dtStatDate,server,account,testId,channelName");
		
		//生成数据同步配置
		DataTaskParam dataParam = DataexParamFactory.getParam(PluginName.MYSQL_READER, sparaMap
				, PluginName.MYSQL_WRITER, tparaMap);
		
		//执行同步程序
		DataexFactory.getEngine()
		.run(dataParam, new DataEngineListener() {
			@Override
			public void notify(final DataEngineResult loadResult) {
				if (loadResult != null) {
					//执行成功，打印出数据行数和字节数
					System.out.println("finsh to load data, data row : " 
							+ loadResult.getTransferedLine() 
							+ ", data quantity : "
							+ loadResult.getByteCount());	
				}
				else {
					System.err.println("load data failed");
				}
			}
		});
	}
	
	
	@Test
	public void test02() throws Exception {
		
		final long startTime = System.currentTimeMillis();
		System.out.println("startTime: " + startTime);
		
		String sSql = "SELECT dtStatDate, server, account, testId, channelId, channelName, dtDetail FROM tb_oss_download_log"; 
		
		Map<String, String> sparaMap = new HashMap<String, String>();
		sparaMap.put("ip", "localhost");
		sparaMap.put("username", "root");
		sparaMap.put("password", "123456");
		sparaMap.put("dbname", "dc_oss");
		sparaMap.put("sql", sSql);
		
		Map<String, String> tparaMap = new HashMap<String, String>();
		tparaMap.put("ip", "localhost");
		tparaMap.put("username", "root");
		tparaMap.put("password", "123456");
		tparaMap.put("dbname", "dc_oss");
		tparaMap.put("replace", "true");
		tparaMap.put("table", "tb_oss_download_log_t");
		
		DataTaskParam dataParam = DataexParamFactory.getParam(PluginName.MYSQL_READER, sparaMap
				, PluginName.MYSQL_WRITER, tparaMap);
		
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
				
				long finishTime = System.currentTimeMillis();
				System.out.println("finishTime: " + finishTime);
				
				System.out.println("costTime: " + (finishTime - startTime));
			}
		});
	}
	
	@Test
	public void test03() throws Exception {
		
		long startTime = System.currentTimeMillis();
		System.out.println("startTime: " + startTime);
		
		String sSql = "SELECT dtStatDate, server, account, testId, channelId, channelName, dtDetail FROM tb_oss_download_log"; 
		
		//Class.forName("com.mysql.jdbc.Driver");
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/dc_oss?characterEncoding=UTF-8&rewriteBatchedStatements=true&useServerPrepStmts=false");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		final List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sSql);
		
		long startWriteTime = System.currentTimeMillis();
		
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
		
		long finishTime = System.currentTimeMillis();
		System.out.println("finishTime: " + finishTime);
		
		System.out.println("writeCostTime: " + (finishTime - startWriteTime));
		System.out.println("totalCostTime: " + (finishTime - startTime));
	}

}
