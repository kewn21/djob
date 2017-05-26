/**
 * This file created at 2014-3-17.
 *
 */
package org.kesy.djob.dex.param;

import java.util.Map;

/**
 * <code>{@link MapParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class MapParam implements DataSourceParam, DataTargetParam {
	private Map<String, String> param;

	/**
	 * @return the param
	 */
	public Map<String, String> getParam() {
		return param;
	}

	public MapParam(Map<String, String> param) {
		this.param = param;
	}
}
