/**
 * This file created at 2014-2-26.
 *
 */
package org.kesy.djob.dex.param;

import java.util.Map;

/**
 * <code>{@link RdbSourceParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public abstract class RdbSourceParam implements DataSourceParam {
	private String ip = "127.0.0.1";
	private String port = "";
	private String dbname = "";
	private String username = "";
	private String password = "";
	private String tables = "";
	private String where = "";
	private String sql = "";
	private String columns = "*";
	private String connurl="";
	private Map<String, String> mapParam;
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the dbname
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * @param dbname
	 *            the dbname to set
	 */
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the tables
	 */
	public String getTables() {
		return tables;
	}

	/**
	 * @param tables
	 *            the tables to set
	 */
	public void setTables(String tables) {
		this.tables = tables;
	}

	/**
	 * @return the where
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * @param where
	 *            the where to set
	 */
	public void setWhere(String where) {
		this.where = where;
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql
	 *            the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * @return the columns
	 */
	public String getColumns() {
		return columns;
	}

	/**
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(String columns) {
		this.columns = columns;
	}

	/**
	 * @return the connurl
	 */
	public String getConnurl() {
		return connurl;
	}

	/**
	 * @param connurl the connurl to set
	 */
	public void setConnurl(String connurl) {
		this.connurl = connurl;
	}
	
	public Map<String, String> getParam() {
		// TODO implement BaseParam.getParam
		return mapParam;
	}

}
