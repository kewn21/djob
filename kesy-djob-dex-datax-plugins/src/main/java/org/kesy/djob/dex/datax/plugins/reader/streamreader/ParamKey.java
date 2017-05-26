package org.kesy.djob.dex.datax.plugins.reader.streamreader;

public final class ParamKey {
	
	/*
	 * @name: fieldSplit
	 * @description: seperator to seperate field
	 * @range:
	 * @mandatory: false 
	 * @default:\t
	 */
	public static final String fieldSplit = "field_split";
	
	/*
	 * @name: encoding
	 * @description: environment encode 
	 * @range: UTF-8|GBK|GB2312
	 * @mandatory: false
	 * @default: UTF-8
	 */
	public static final String encoding = "encoding";
	
	/*
       * @name: nullString
       * @description: replace nullString to null
       * @range: 
       * @mandatory: false
       * @default: \N
       */
	public static final String nullString = "null_string";

	 /*
       * @name:concurrency
       * @description:concurrency of the job
       * @range:1-100
       * @mandatory: false
       * @default:1
       */
	public static final String concurrency = "concurrency";
	
	public static final String filepath = "fromfilepath";
	
	public static final String hasHeader = "has_header";
}
