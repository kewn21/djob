/**
 * This file created at 2014-3-5.
 *

 */
package org.kesy.djob.dex.datax.param;


/**
 * <code>{@link DataxSourcePlusParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class DataxSourcePlusParam {
	private DataxCommonParam commonParam;

	private DataxSourcePlusParam() {

	}

	/**
	 * @return the commonParam
	 */
	public DataxCommonParam getCommonParam() {
		return commonParam;
	}

	/**
	 * @param commonParam
	 *            the commonParam to set
	 */
	private void setCommonParam(DataxCommonParam commonParam) {
		this.commonParam = commonParam;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static final class Builder {
		private String encoding = "UTF-8";

		/**
		 * @param encoding
		 *            the encoding to set
		 */
		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}

		private Builder() {

		}

		public DataxSourcePlusParam build() {
			DataxCommonParam commonParam = new DataxCommonParam(encoding);
			DataxSourcePlusParam param = new DataxSourcePlusParam();
			param.setCommonParam(commonParam);
			return param;
		}
	}

}
