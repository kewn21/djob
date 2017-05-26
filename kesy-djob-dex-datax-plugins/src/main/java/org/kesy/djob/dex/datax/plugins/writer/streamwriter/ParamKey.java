package org.kesy.djob.dex.datax.plugins.writer.streamwriter;

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
	 * @description: stream encode
	 * @range: UTF-8|GBK|GB2312
	 * @mandatory: false
	 * @default: UTF-8
	 */
	public static final String encoding = "encoding";
	/*
	 * @name: prefix 
	 * @description:  print result with prefix
	 * @range: 
	 * @mandatory: false
	 * @default:
	 */
	public static final String prefix = "prefix";
	
	/*
	 * @name: print
	 * @description: print the result
	 * @range: 
	 * @mandatory: false
	 * @default: true
	 */
	public static final String print = "print";
	
	/*
	 * @name: nullchar
	 * @description:  replace null with the nullchar
	 * @range: 
	 * @mandatory: false
	 * @default: 
	 */
	public static final String nullChar = "nullchar";

	 /*
       * @name:concurrency
       * @description:concurrency of the job
       * @range:1
       * @mandatory: false
       * @default:1
       */
	public static final String concurrency = "concurrency";
	
	
	public static final String tofilepath = "tofilepath";
}

