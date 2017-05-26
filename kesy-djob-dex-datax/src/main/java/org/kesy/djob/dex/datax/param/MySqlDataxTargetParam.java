/**
 * This file created at 2014-2-26.
 *
 */
package org.kesy.djob.dex.datax.param;

import org.kesy.djob.dex.param.MySqlTargetParam;
import org.kesy.djob.dex.param.PluginName;
import org.kesy.djob.dex.utils.DBConnUrl;

/**
 * <code>{@link MySqlDataxTargetParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public final class MySqlDataxTargetParam extends MySqlTargetParam implements
IDataxParam {

	private String set = "";
	private String replace = "";
	private String mysql_params = "";
	private PluginName pluginid = PluginName.MYSQL_WRITER;
	private String concurrency = "1";
	private DataxSourcePlusParam plusParam;

	public MySqlDataxTargetParam(String ip, String dbname, String username,
			String password,String table) {
		super(ip, dbname, username, password,table);
	}

	public MySqlDataxTargetParam(String ip, String port,String dbname, String username,
			String password, String table) {
		super(ip, port,dbname, username, password, table);
	}
	
	public MySqlDataxTargetParam(DBConnUrl url, String username,
			String password, String table) {
		super(url, username, password, table);
	}

	/**
	 * @return the set
	 */
	public String getSet() {
		return set;
	}

	/**
	 * @param set
	 *            the set to set
	 */
	public void setSet(String set) {
		this.set = set;
	}

	/**
	 * @return the replace
	 */
	public String getReplace() {
		return replace;
	}

	/**
	 * @param replace
	 *            the replace to set
	 */
	public void setReplace(String replace) {
		this.replace = replace;
	}

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
	 * org.kesy.djob.dex.datax.core.modle.DataxTargetParam#getConcurrency()
	 */
	@Override
	public String getConcurrency() {
		// TODO implement DataxTargetParam.getConcurrency
		return this.concurrency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.core.modle.DataxTargetParam#getPlusParam()
	 */
	@Override
	public DataxSourcePlusParam getPlusParam() {
		// TODO implement DataxTargetParam.getPlusParam
		if (null == plusParam) {
			return DataxSourcePlusParam.newBuilder().build();
		}
		return plusParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.core.modle.DataxTargetParam#setConcurrency
	 * (java.lang.String)
	 */
	@Override
	public void setConcurrency(String concurrency) {
		this.concurrency = concurrency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.core.modle.DataxTargetParam#setPlusParam(org.kesy.djob.core.modle.DataxSourcePlusParam)
	 */
	@Override
	public void setPlusParam(DataxSourcePlusParam plusParam) {
		this.plusParam = plusParam;
	}
}
