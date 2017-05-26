/**
 * This file created at 2014-3-5.
 *
 */
package org.kesy.djob.dex.param;

import java.util.Map;

/**
 * <code>{@link StreamSourceParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class StreamSourceParam implements DataSourceParam {
	private String fromfilepath;
	private Map<String, String> mapParam;

	public StreamSourceParam(String fromfilepath) {
		this.fromfilepath = fromfilepath;
	}

	/**
	 * @return the fromFilePath
	 */
	public String getFromfilepath() {
		return fromfilepath;
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
