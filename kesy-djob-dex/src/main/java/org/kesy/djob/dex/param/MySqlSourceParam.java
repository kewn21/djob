/**
 * This file created at 2014-3-5.
 *
 */
package org.kesy.djob.dex.param;

import org.kesy.djob.dex.utils.DBConnUrl;

/**
 * <code>{@link MySqlSourceParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class MySqlSourceParam extends RdbSourceParam {
	public MySqlSourceParam(String ip, String dbname, String username,
			String password, String tables, String where,String columns) {
		super.setIp(ip);
		super.setDbname(dbname);
		super.setUsername(username);
		super.setPassword(password);
		super.setTables(tables);
		super.setWhere(where);
		super.setPort("3306");
		super.setColumns(columns);
	}

	public MySqlSourceParam(String ip, String dbname, String username,
			String password, String sql) {
		super.setIp(ip);
		super.setDbname(dbname);
		super.setUsername(username);
		super.setPassword(password);
		super.setSql(sql);
		super.setPort("3306");
	}
	
	public MySqlSourceParam(String ip, String port,String dbname, String username,
			String password, String sql) {
		super.setIp(ip);
		super.setDbname(dbname);
		super.setUsername(username);
		super.setPassword(password);
		super.setSql(sql);
		super.setPort(port);
	}

	public MySqlSourceParam(DBConnUrl jdbcConnectString, String username,
			String password, String sql) {
		super.setIp(jdbcConnectString.getHostName());
		super.setPort(String.valueOf(jdbcConnectString.getPort()));
		super.setDbname(jdbcConnectString.getDatabaseName());
		super.setUsername(username);
		super.setPassword(password);
		super.setSql(sql);
		super.setConnurl(jdbcConnectString.getConnectString());
	}

	public MySqlSourceParam(DBConnUrl jdbcConnectString, String username,
			String password, String tables, String where, String columns) {
		super.setIp(jdbcConnectString.getHostName());
		super.setPort(String.valueOf(jdbcConnectString.getPort()));
		super.setDbname(jdbcConnectString.getDatabaseName());
		super.setUsername(username);
		super.setPassword(password);
		super.setTables(tables);
		super.setWhere(where);
		super.setColumns(columns);
		super.setConnurl(jdbcConnectString.getConnectString());
	}
	
}
