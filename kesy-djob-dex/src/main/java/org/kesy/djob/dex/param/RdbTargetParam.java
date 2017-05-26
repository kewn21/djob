/**
 * This file created at 2014-2-26.
 *
 */
package org.kesy.djob.dex.param;

import java.util.Map;



/**
 * <code>{@link RdbTargetParam}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public abstract class RdbTargetParam implements DataSourceParam {
	
	private String ip="127.0.0.1";
	private String port="";
	private String dbname="";
	private String username="";
	private String password="";
	private String table="";
	private String colorder="";
	private String limit="";
	private String pre="";
	private String post="";
	private String connurl="";
	private Map<String, String> mapParam;
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
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
	 * @param port the port to set
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
	 * @param dbname the dbname to set
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
	 * @param username the username to set
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
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the tables
	 */
	public String getTable() {
		return table;
	}
	/**
	 * @param tables the tables to set
	 */
	public void setTable(String table) {
		this.table = table;
	}
	/**
	 * @return the colorder
	 */
	public String getColorder() {
		return colorder;
	}
	/**
	 * @param colorder the colorder to set
	 */
	public void setColorder(String colorder) {
		this.colorder = colorder;
	}
	
	/**
	 * @return the limit
	 */
	public String getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}
	/**
	 * @return the pre
	 */
	public String getPre() {
		return pre;
	}
	/**
	 * @param pre the pre to set
	 */
	public void setPre(String pre) {
		this.pre = pre;
	}
	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}
	/**
	 * @param post the post to set
	 */
	public void setPost(String post) {
		this.post = post;
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
