/**
 * This file created at 2014-2-26.
 *
 */
package org.kesy.djob.dex.datax.param;

import org.kesy.djob.dex.param.MySqlSourceParam;
import org.kesy.djob.dex.param.PluginName;
import org.kesy.djob.dex.utils.DBConnUrl;

/**
 * <code>{@link MySqlDataxSourceParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public final class MySqlDataxSourceParam extends MySqlSourceParam implements
		IDataxParam {

	/**
	 * @param ip
	 * @param dbname
	 * @param username
	 * @param password
	 * @param sql
	 */
	public MySqlDataxSourceParam(String ip, String dbname, String username,
			String password, String sql) {
		super(ip, dbname, username, password, sql);
	}

	public MySqlDataxSourceParam(String ip, String port, String dbname,
			String username, String password, String sql) {
		super(ip, port, dbname, username, password, sql);
	}

	public MySqlDataxSourceParam(String ip, String dbname, String username,
			String password, String tables, String where, String columns) {
		super(ip, dbname, username, password, tables, where, columns);
	}

	public MySqlDataxSourceParam(DBConnUrl url, String username, String password,
			String tables, String where, String columns) {
		super(url, username, password, tables, where, columns);
	}
	
	public MySqlDataxSourceParam(DBConnUrl url, String username, String password, String sql) {
		super(url, username, password, sql);
	}

	private String mysql_params = "";

	private PluginName pluginid = PluginName.MYSQL_READER;

	private String concurrency = "1";

	private DataxSourcePlusParam plusParam;

	/**
	 * @return the mysql_params
	 */
	public String getMysql_params() {
		return mysql_params;
	}

	/**
	 * @param mysqlParams
	 *            the mysql_params to set
	 */
	public void setMysql_params(String mysqlParams) {
		mysql_params = mysqlParams;
	}

	/**
	 * @return the pluginid
	 */
	public PluginName getPluginId() {
		return pluginid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.core.modle.DataxSourceParam#getPlusParam()
	 */
	@Override
	public DataxSourcePlusParam getPlusParam() {
		if (null == plusParam) {
			return DataxSourcePlusParam.newBuilder().build();
		}
		return plusParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.core.modle.DataxSourceParam#setConcurrency
	 * (java.lang.String)
	 */
	@Override
	public void setConcurrency(String concurrency) {
		// TODO implement DataxSourceParam.setConcurrency
		this.concurrency = concurrency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.core.modle.DataxSourceParam#getConcurrency()
	 */
	@Override
	public String getConcurrency() {
		// TODO implement DataxSourceParam.getConcurrency
		return this.concurrency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.core.modle.DataxSourceParam#setPlusParam(org.kesy.djob.core.modle.DataxSourcePlusParam)
	 */
	@Override
	public void setPlusParam(DataxSourcePlusParam plusParam) {
		this.plusParam = plusParam;
	}

}
