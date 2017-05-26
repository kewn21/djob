/**
 * This file created at 2014-3-6.
 *
 */
package org.kesy.djob.dex.datax.param;

import org.kesy.djob.dex.param.FTPTargetParam;
import org.kesy.djob.dex.param.PluginName;

/**
 * <code>{@link FTPDataxTargrtParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public final class FTPDataxTargrtParam extends FTPTargetParam implements
IDataxParam {

	private String concurrency = "1";
	private DataxSourcePlusParam plusParam;
	private PluginName pluginid = PluginName.FTP_WRITER;

	/**
	 * @param host
	 * @param username
	 * @param password
	 * @param path
	 * @param filename
	 */
	public FTPDataxTargrtParam(String host, String username, String password,
			String path, String filename) {
		super(host, username, password, path, filename);
	}

	/**
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param path
	 * @param filename
	 */
	public FTPDataxTargrtParam(String host, String port, String username,
			String password, String path, String filename) {
		super(host, port, username, password, path, filename);
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
	 * @see org.kesy.djob.dex.datax.core.modle.DataxTargetParam#getPluginid()
	 */
	@Override
	public PluginName getPluginId() {
		// TODO implement DataxTargetParam.getPluginid
		return pluginid;
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
		// TODO implement DataxTargetParam.setConcurrency
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
		// TODO implement DataxTargetParam.setPlusParam
		this.plusParam = plusParam;

	}

}
