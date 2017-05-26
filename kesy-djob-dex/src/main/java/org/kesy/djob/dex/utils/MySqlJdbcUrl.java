/**
 * This file created at 2014-3-12.
 *

 */
package org.kesy.djob.dex.utils;

/**
 * <code>{@link MySqlJdbcUrl}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 * 
 * @URL:jdbc:mysql://<host>:<port>/<database_name>
 */
public class MySqlJdbcUrl implements IJdbcUrl {
	private String port;
	private String host;
	private String databaseName;
	public static final String CONN_TYPE="jdbc:mysql://<host>:<port>/<database_name>";

	public MySqlJdbcUrl(String host, String port, String databaseName) {
		this.port = port.trim();
		this.host = host.trim();
		this.databaseName = databaseName.trim();
	}

	@Override
	public String getDatabaseName() {
		// TODO implement MySqlJdbcUrl.getDatabaseName
		return this.databaseName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.data.util.IJdbcUrl#getHost()
	 */
	@Override
	public String getHostName() {
		// TODO implement MySqlJdbcUrl.getHost
		return this.host;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.data.util.IJdbcUrl#getPort()
	 */
	@Override
	public String getPort() {
		// TODO implement MySqlJdbcUrl.getPort
		return this.port;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.data.util.IJdbcUrl#getJdbcType()
	 */
	@Override
	public String getJdbcType() {
		// TODO implement IJdbcUrl.getJdbcType
		return CONN_TYPE;
	}

}
