/**
 * This file created at 2014-2-26.
 *

 */
package org.kesy.djob.dex.datax.param;

import org.kesy.djob.dex.param.PluginName;
import org.kesy.djob.dex.param.StreamTargetParam;

/**
 * <code>{@link StreamDataxTargetParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public final class StreamDataxTargetParam extends StreamTargetParam implements IDataxParam{
	private String field_split = "\\t";
	private String nullchar = "";

	private PluginName pluginid = PluginName.STREAM_WRITER;
	private String concurrency = "1";
	private DataxSourcePlusParam plusParam;
	public StreamDataxTargetParam(String toFilePath) {
		super(toFilePath);
	}

	/**
	 * @return the field_split
	 */
	public String getField_split() {
		return field_split;
	}

	/**
	 * @param fieldSplit
	 *            the field_split to set
	 */
	public void setField_split(String fieldSplit) {
		field_split = fieldSplit;
	}

	/**
	 * @return the nullchar
	 */
	public String getNullchar() {
		return nullchar;
	}

	/**
	 * @param nullchar
	 *            the nullchar to set
	 */
	public void setNullchar(String nullchar) {
		this.nullchar = nullchar;
	}

	/**
	 * @return the pluginid
	 */
	public PluginName getPluginId() {
		return pluginid;
	}

	/**
	 * @return the concurrency
	 */
	public String getConcurrency() {
		return concurrency;
	}

	/**
	 * @param concurrency the concurrency to set
	 */
	public void setConcurrency(String concurrency) {
		this.concurrency = concurrency;
	}

	/**
	 * @return the plusParam
	 */
	public DataxSourcePlusParam getPlusParam() {
		if(null==plusParam)
		{
			return DataxSourcePlusParam.newBuilder().build();
		}
		return plusParam;
	}

	/**
	 * @param plusParam the plusParam to set
	 */
	public void setPlusParam(DataxSourcePlusParam plusParam) {
		this.plusParam = plusParam;
	}

}
