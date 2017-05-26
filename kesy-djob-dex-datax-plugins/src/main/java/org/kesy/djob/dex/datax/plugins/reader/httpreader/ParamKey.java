package org.kesy.djob.dex.datax.plugins.reader.httpreader;

public final class ParamKey {
	
	/* 
	 * @name: URLDelimiter
	 * @description: how to split url
	 * @range:
	 * @mandatory: false
	 * @default: ;
	 */
	public static final String URLDelimiter = "urldelimiter";
	
	/*
	 * @name: fieldSplit
	 * @description: separator to split urls
	 * @range:
	 * @mandatory: false
	 * @default:\t
	 */
	public static final String fieldSplit = "field_split";

	/*
	 * @name: encoding
	 * @description: encode 
	 * @range: UTF-8|GBK|GB2312
	 * @mandatory: false
	 * @default: UTF-8
	 */
	public static final String encoding = "encoding";

	/*
	 * @name: nullString
	 * @description: replace this nullString to null
	 * @range:
	 * @mandatory: false
	 * @default: \N
	 */
	public static final String nullString = "null_string";
	
	
	/*
	 * @name: httpURLs
	 * @description: url to fetch data
	 * @range:legal http url
	 * @mandatory: true
	 * @default:
	 */
	public static final String httpURLs = "httpurls";

	 /*
       * @name:concurrency
       * @description:concurrency of the job
       * @range:1-100
       * @mandatory: false
       * @default:1
       */
	public static final String concurrency = "concurrency";
}
