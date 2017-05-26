package org.kesy.djob.dex.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Some utilities for parsing JDBC URLs which may not be tolerated by Java's
 * java.net.URL class. java.net.URL does not support multi:part:scheme://
 * components, which virtually all JDBC connect string URLs have.
 */
public final class DBConnUrl implements IJdbcUrl {

	public static final Log LOG = LogFactory.getLog(DBConnUrl.class.getName());

	private String connectString;

	private IJdbcUrl url;

	private static final String MYSQL = "jdbc:mysql://";

	public DBConnUrl(String connectString) {
		this.connectString = connectString.trim();
		BuildJdbcUrl();
	}

	private void BuildJdbcUrl() {
		String templeteString = null;
		String _mainString = null;
		String _host;
		String _port;
		String _dbname;
		try {
			if (connectString.startsWith(MYSQL)) {
				templeteString = MySqlJdbcUrl.CONN_TYPE;
				// jdbc:mysql://<host>:<port>/<database_name>
				_mainString = getMainConnString(MYSQL.length());
				_host = _mainString.substring(0, _mainString.indexOf(":"));
				_port = _mainString.substring(_mainString.indexOf(":") + 1,
						_mainString.indexOf("/"));
				_dbname = _mainString
						.substring(_mainString.indexOf("/") + 1);
				this.url = new MySqlJdbcUrl(_host, _port, _dbname);

			} 
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("JdbcUrl is error,Please check..");
			LOG.error("The correct JdbcUrl shoud like this:"
					+ templeteString);
		}

		// System.out.println("_mainString"+_mainString);
	}

	private String getMainConnString(int beginIndex) {
		if (connectString.contains("?")) {
			return connectString.substring(beginIndex, connectString
					.indexOf("?"));
		} else {
			return connectString.substring(beginIndex);
		}
	}

	public String getConnectString() {
		return connectString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.data.util.IJdbcUrl#getDatabaseName()
	 */
	@Override
	public String getDatabaseName() {
		// TODO implement IJdbcUrl.getDatabaseName
		return url.getDatabaseName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.data.util.IJdbcUrl#getHost()
	 */
	@Override
	public String getHostName() {
		// TODO implement IJdbcUrl.getHost
		return url.getHostName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.data.util.IJdbcUrl#getPort()
	 */
	@Override
	public String getPort() {
		// TODO implement IJdbcUrl.getPort
		return url.getPort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.data.util.IJdbcUrl#getJdbcType()
	 */
	@Override
	public String getJdbcType() {
		// TODO implement IJdbcUrl.getJdbcType
		return url.getJdbcType();
	}

}
