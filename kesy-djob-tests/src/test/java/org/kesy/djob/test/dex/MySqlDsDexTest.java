/**
 * @date 2014年8月20日
 */
package org.kesy.djob.test.dex;

import org.junit.Test;
import org.kesy.djob.dex.DataexFactory;
import org.kesy.djob.dex.ds.DatasourceParamGenerator;
import org.kesy.djob.dex.engine.DataEngineListener;
import org.kesy.djob.dex.engine.DataEngineResult;
import org.kesy.djob.dex.engine.DataTaskParam;

/**
 * @author kewn
 */
public class MySqlDsDexTest {
	
	@Test
	public void test01() throws Exception {
		
		String sSql = "SELECT DISTINCT DATE_FORMAT(lr.tReportTime, '%Y-%m-%d'), '-1', l.iUdbUid, 2, lr.vChannel  "
				+ "FROM tbloginreportlog2005 lr JOIN tbloginlog2005 l  "
				+ "ON lr.iGameId = l.iGameId and lr.iYyuid = l.iYyuid "
				+ "and date_format(lr.tReportTime, '%Y-%m-%d') = date_format(l.tLogTime, '%Y-%m-%d') "
				+ "WHERE lr.iResult = 0 and lr.iGameId = '2005' "
				+ "and lr.vChannel IS NOT NULL AND TRIM(lr.vChannel) != '' "
				+ "and date_format(lr.tReportTime, '%Y-%m-%d') = '2014-08-12'"; 

		//创建数据源参数，ds02和ds01分别为源和目标的数据源名
		DatasourceParamGenerator paramGenerator = new DatasourceParamGenerator("ds02", "ds01");
		paramGenerator.addSourceParam("sql", sSql); //源的提数sql
		paramGenerator.addTargetParam("replace", "true");
		paramGenerator.addTargetParam("table", "tb_oss_download_log_t"); //写到的目标表
		paramGenerator.addTargetParam("colorder", "dtStatDate, server, account, testId, channelName");
		
		//生成数据同步配置
		DataTaskParam dataParam = paramGenerator.build();
		
		//执行同步程序
		DataexFactory.getEngine()
		.run(dataParam, new DataEngineListener() {
			@Override
			public void notify(final DataEngineResult loadResult) {
				if (loadResult != null) {
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
}
