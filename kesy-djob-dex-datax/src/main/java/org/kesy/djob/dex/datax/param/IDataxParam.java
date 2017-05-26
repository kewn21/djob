/**
 * This file created at 2014-2-26.
 *

 */
package org.kesy.djob.dex.datax.param;

import java.util.Map;

import org.kesy.djob.dex.param.PluginName;

/**
 * <code>{@link IDataxParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public interface IDataxParam {
	/**
	 * @return the concurrency
	 */
	public String getConcurrency();

	/**
	 * @param concurrency
	 *            the concurrency to set
	 */
	public void setConcurrency(String concurrency);

	/**
	 * @return the pluginid
	 */
	public PluginName getPluginId();

	public DataxSourcePlusParam getPlusParam();

	public void setPlusParam(DataxSourcePlusParam plusParam);
	
	public Map<String, String> getParam();
}
