/**
 * This file created at 2014-3-6.
 *
 */
package org.kesy.djob.dex.param;

import java.util.Map;

/**
 * <code>{@link FTPTargetParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class FTPTargetParam implements DataTargetParam {
	private String host = "127.0.0.1";
	private String port = "21";
	private String username = "";
	private String password = "";
	private String path = "/";
	private String filename = "";
	
	private Map<String, String> mapParam;

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	public FTPTargetParam(String host, String port, String username,
			String password, String path, String filename) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.path = path;
		this.filename = filename;
	}

	public FTPTargetParam(String host, String username, String password,
			String path, String filename) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.path = path;
		this.filename = filename;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.dex.engine.BaseParam#getParam()
	 */
	@Override
	public Map<String, String> getParam() {
		// TODO implement BaseParam.getParam
		return mapParam;
	}
}
