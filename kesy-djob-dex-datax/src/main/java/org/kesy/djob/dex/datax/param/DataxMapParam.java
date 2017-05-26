/**
 * This file created at 2014-3-17.
 *
 */
package org.kesy.djob.dex.datax.param;

import java.util.Map;

import org.kesy.djob.dex.param.MapParam;
import org.kesy.djob.dex.param.PluginName;

/**
 * <code>{@link DataxMapParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class DataxMapParam extends MapParam implements IDataxParam {

	/**
	 * @param param
	 */
	private PluginName pluginId;
	private DataxSourcePlusParam plusParam;
	private String concurrency = "1";

	public DataxMapParam(Map<String, String> param, PluginName pluginType) {
		super(param);
		pluginId = pluginType;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.datax.core.modle.IDataxParam#getConcurrency()
	 */
	@Override
	public String getConcurrency() {
		return concurrency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.datax.core.modle.IDataxParam#getPluginid()
	 */
	@Override
	public PluginName getPluginId() {
		return pluginId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.datax.core.modle.IDataxParam#getPlusParam()
	 */
	@Override
	public DataxSourcePlusParam getPlusParam() {
		return plusParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.core.modle.IDataxParam#setConcurrency(java
	 * .lang.String)
	 */
	@Override
	public void setConcurrency(String concurrency) {
		this.concurrency = concurrency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.datax.core.modle.IDataxParam#setPlusParam(org.kesy.djob.core.modle.DataxSourcePlusParam)
	 */
	@Override
	public void setPlusParam(DataxSourcePlusParam plusParam) {
		this.plusParam = plusParam;
	}

}
