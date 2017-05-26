/**
 * This file created at 2014-3-5.
 *
 */
package org.kesy.djob.dex.param;

import org.kesy.djob.dex.utils.DBConnUrl;

/**
 * <code>{@link MySqlTargetParam}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class MySqlTargetParam extends RdbTargetParam {
	public MySqlTargetParam(String ip,String dbname,String username,String password,String table){
		super.setIp(ip);
		super.setDbname(dbname);
		super.setUsername(username);
		super.setPassword(password);
		super.setTable(table);
		super.setPort("3306");
	}
	
	
	public MySqlTargetParam(String ip,String port,String dbname,String username,String password,String table){
		super.setIp(ip);
		super.setPort(port);
		super.setDbname(dbname);
		super.setUsername(username);
		super.setPassword(password);
		super.setTable(table);
		
	}
	
	
	public MySqlTargetParam(DBConnUrl jdbcConnectString, String username,
			String password, String table) {
		super.setIp(jdbcConnectString.getHostName());
		super.setPort(String.valueOf(jdbcConnectString.getPort()));
		super.setDbname(jdbcConnectString.getDatabaseName());
		super.setUsername(username);
		super.setPassword(password);
		super.setTable(table);
		super.setConnurl(jdbcConnectString.getConnectString());
	}
}
