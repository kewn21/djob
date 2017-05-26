
package org.kesy.djob.dex.datax.plugins.reader.mysqlreader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.kesy.djob.dex.datax.common.exception.DataExchangeException;
import org.kesy.djob.dex.datax.common.exception.ExceptionTracker;
import org.kesy.djob.dex.datax.common.plugin.LineSender;
import org.kesy.djob.dex.datax.common.plugin.MetaData;
import org.kesy.djob.dex.datax.common.plugin.PluginParam;
import org.kesy.djob.dex.datax.common.plugin.PluginStatus;
import org.kesy.djob.dex.datax.common.plugin.Reader;
import org.kesy.djob.dex.datax.engine.conf.PluginPropertyConf;
import org.kesy.djob.dex.datax.plugins.common.DBResultSetSender;
import org.kesy.djob.dex.datax.plugins.common.DBSource;
import org.kesy.djob.dex.datax.plugins.common.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

public class MySqlReader extends Reader {
	private Connection conn;

	/* below for job-xml variant */
	private String encode = null;

	private String username = "";

	private String password = "";

	private String ip = "";

	private String port = "3306";

	private String dbname = null;

	private int concurrency = -1;

	private String mysqlParams;

	private String sql = null;

	private String connurl = "";

	private String sourceUniqKey = "";
	
	private static Logger logger = LoggerFactory.getLogger(MySqlReader.class);
	
	private static String DRIVER_NAME = "com.mysql.jdbc.Driver";

	@Override
	public int init() {
		/* for database connection */
		this.username = param.getValue(ParamKey.username, this.username);
		this.password = param.getValue(ParamKey.password, this.password);
		this.ip = param.getValue(ParamKey.ip, "");
		this.port = param.getValue(ParamKey.port, this.port);
		this.dbname = param.getValue(ParamKey.dbname, "");
		this.encode = param.getValue(ParamKey.encoding, "");
		this.mysqlParams = param.getValue(ParamKey.mysqlParams, "");
		this.connurl = param.getValue(ParamKey.connurl,"");
		/* set nullChar to replace null in query-null-value */
		this.sql = param.getValue(ParamKey.sql, "").trim();
		/* for connection session */
		this.concurrency = param.getIntValue(ParamKey.concurrency, 1);
		
		this.sourceUniqKey = DBSource.genKey(this.getClass(), ip, port,
				dbname, connurl, username);

		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int prepare(PluginParam param) {
		this.init();
		Properties p = createProperties();
		DBSource.register(this.getClass(), this.ip, this.port, this.dbname, this.connurl, this.username, p);
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int connect() {
		conn = DBSource.getConnection(this.getClass(), ip, port, dbname, connurl, username);
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int startRead(LineSender lineSender) {
		DBResultSetSender proxy = DBResultSetSender.newSender(lineSender);
		proxy.setMonitor(getMonitor());
		proxy.setDateFormatMap(genDateFormatMap());

		String sql = param.getValue(ParamKey.sql);
		logger.info(String.format("MySqlReader start to query %s .", sql));
		ResultSet rs = null;
		try {
			rs = DBUtils.query(conn, sql);
			proxy.sendToWriter(rs);
			proxy.flush();
			
			getMonitor().setStatus(PluginStatus.READ_OVER);
			
			return PluginStatus.SUCCESS.value();
		} catch (SQLException e) {
			logger.error(ExceptionTracker.trace(e));
			throw new DataExchangeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			DataSourceUtils.releaseConnection(this.conn, DBSource.getDataSource(this.sourceUniqKey));
		}

	}

	@Override
	public int finish() {
		DataSourceUtils.releaseConnection(this.conn, DBSource.getDataSource(this.sourceUniqKey));
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public List<PluginParam> split(PluginParam param) {
		List<PluginParam> sqlList;

		if (StringUtils.isBlank(this.sql)) {
			/* non-user-defined sql */
			MySqlReaderSplitter spliter = new MySqlReaderSplitter(param);
			spliter.init();
			sqlList = spliter.split();
		} else {
			/* user-define sql */
			sqlList = super.split(param);
		}

		String sql = sqlList.get(0).getValue(ParamKey.sql);
		MetaData m = null;
		try {
			conn = DBSource.getConnection(this.getClass(), ip, port, dbname, connurl, username);
			m = DBUtils.genMetaData(conn, sql);
			param.setMyMetaData(m);
		} catch (SQLException e) {
			logger.error(ExceptionTracker.trace(e));
			throw new DataExchangeException(e);
		} finally {
			DataSourceUtils.releaseConnection(this.conn, DBSource.getDataSource(this.sourceUniqKey));
		}

		return sqlList;
	}

	private Map<String, SimpleDateFormat> genDateFormatMap() {
		Map<String, SimpleDateFormat> mapDateFormat = new HashMap<String, SimpleDateFormat>();
		mapDateFormat.clear();
		mapDateFormat.put("datetime", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss"));
		mapDateFormat.put("timestamp", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss"));
		mapDateFormat.put("time", new SimpleDateFormat("HH:mm:ss"));
		return mapDateFormat;
	}

	private Properties createProperties() {
		Properties p = new Properties();

		String encodeDetail = "";

		if (StringUtils.isNotEmpty(this.encode)) {
			encodeDetail = "useUnicode=true&characterEncoding=" + this.encode + "&";
		}
		String url;
		if (StringUtils.isEmpty(connurl)) {
			url = "jdbc:mysql://" + this.ip + ":" + this.port + "/"
			+ this.dbname + "?" + encodeDetail
			+ "yearIsDateType=false&zeroDateTimeBehavior=convertToNull"
			+ "&defaultFetchSize=" + Integer.MIN_VALUE;
		} else {
			url = connurl;
		}

		if (!StringUtils.isBlank(this.mysqlParams)) {
			url = url + "&" + this.mysqlParams;
		}

		p.setProperty("driverClassName", DRIVER_NAME);
		p.setProperty("url", url);
		p.setProperty("username", username);
		p.setProperty("password", password);
		
		p.setProperty("maxActive", String.valueOf((concurrency + 2) > PluginPropertyConf.getJdbcMaxActive() ? 
				(concurrency + 2) : PluginPropertyConf.getJdbcMaxActive()));
		p.setProperty("initialSize", String.valueOf((concurrency + 2) > PluginPropertyConf.getJdbcInitialSize()? 
				(concurrency + 2) : PluginPropertyConf.getJdbcInitialSize()));
		
		p.setProperty("maxIdle", String.valueOf(PluginPropertyConf.getJdbcMaxIdle()));
		p.setProperty("minIdle", String.valueOf(PluginPropertyConf.getJdbcMinIdle()));
		
		p.setProperty("maxWait", "1000");
		p.setProperty("defaultReadOnly", "true");
		p.setProperty("testOnBorrow", "true");
		p.setProperty("validationQuery", "select 1 from dual");

		logger.info(String.format("MySqlReader try connection: %s .", url));
		return p;
	}

}
