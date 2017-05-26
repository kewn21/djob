/**
 * This file created at 2014-3-5.
 *

 */
package org.kesy.djob.dex.datax.param;

/**
 * <code>{@link DataxCommonParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class DataxCommonParam {


	private String encoding = "UTF-8";

	public DataxCommonParam(String encoding) {
		this.encoding = encoding;
	}



	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

}
