/**
 * This file created at 2014-3-5.
 *
 */
package org.kesy.djob.dex.param;

import java.util.Map;

/**
 * <code>{@link StreamTargetParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class StreamTargetParam implements DataTargetParam {
	private String tofilepath;
	private Map<String, String> mapParam;
	public StreamTargetParam(String tofilepath) {
		this.tofilepath = tofilepath;
	}

	

	/**
	 * @return the toFilePath
	 */
	public String getTofilepath() {
		return tofilepath;
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
