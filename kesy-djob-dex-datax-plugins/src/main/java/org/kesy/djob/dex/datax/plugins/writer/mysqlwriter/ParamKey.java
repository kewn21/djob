package org.kesy.djob.dex.datax.plugins.writer.mysqlwriter;

public final class ParamKey {
	 /*
      * @name: ip
      * @description: MySql database ip address
      * @range:
      * @mandatory: true
      * @default:
      */
	public static final String ip = "ip";
	/*
      * @name: port
      * @description: MySql database port
      * @range:
      * @mandatory: true
      * @default:3306
      */
	public static final String port = "port";
	/*
      * @name: dbname
      * @description: MySql database name
      * @range:
      * @mandatory: true
      * @default:
      */
	public static final String dbname = "dbname";
	/*
      * @name: username
      * @description: MySql database login username
      * @range:
      * @mandatory: true
      * @default:
      */
	public static final String username = "username";
	/*
      * @name: password
      * @description: MySql database login password
      * @range:
      * @mandatory: true
      * @default:
      */
	public static final String password = "password";
	/*
      * @name: table
      * @description: table to be dumped data into
      * @range: 
      * @mandatory: true
      * @default: 
      */
	public static final String table = "table";
	/*
      * @name: colorder
      * @description: order of columns
      * @range: 
      * @mandatory: false
      * @default:
      */
	public static final String colorder = "colorder";
	/*
      * @name: encoding
      * @description: 
      * @range: UTF-8|GBK|GB2312
      * @mandatory: false
      * @default: UTF-8
      */
	public static final String encoding = "encoding";
	/*
	 * @name: pre
	 * @description: execute sql before dumping data
	 * @range:
	 * @mandatory: false
	 * @default:
	 */
	public static final String pre = "pre";
	/*
	 * @name: post
	 * @description: execute sql after dumping data
	 * @range:
	 * @mandatory: false
	 * @default:
	 */
	public static final String post = "post";

	/*
	 * @name: limit
	 * @description: error limit
	 * @range: [0-65535]
	 * @mandatory: false
	 * @default: 0
	 */
	public static final String limit = "limit";
	/*
	 * @name: set
	 * @description:
	 * @range:
	 * @mandatory: false
	 * @default:
	 */
	public static final String set = "set";
	/*
	 * @name: replace
	 * @description:
	 * @range: [true/false]
	 * @mandatory: false
	 * @default:false
	 */
	public static final String replace = "replace";
     /*
      * @name:mysql.params
      * @description:mysql driver params
      * @range:params1|params2|...
      * @mandatory: false
      * @default:
      */
	public static final String mysqlParams = "mysql.params";

	 /*
      * @name:concurrency
      * @description:concurrency of the job
      * @range:1-100
      * @mandatory: false
      * @default:1
      */
	public static final String concurrency = "concurrency";
	
	
	public static final String connurl = "connurl";
	
}

