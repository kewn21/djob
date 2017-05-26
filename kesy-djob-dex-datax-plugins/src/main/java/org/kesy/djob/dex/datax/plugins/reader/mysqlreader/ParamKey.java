package org.kesy.djob.dex.datax.plugins.reader.mysqlreader;

public class ParamKey {
		 /*
	       * @name: ip
	       * @description: MySql database's ip address
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public static final String ip = "ip";
		/*
	       * @name: port
	       * @description: MySql database's port
	       * @range:
	       * @mandatory: true
	       * @default:3306
	       */
		public static final String port = "port";
		/*
	       * @name: dbname
	       * @description: MySql database's name
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public static final String dbname = "dbname";
		/*
	       * @name: username
	       * @description: MySql database's login name
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public static final String username = "username";
		/*
	       * @name: password
	       * @description: MySql database's login password
	       * @range:
	       * @mandatory: true
	       * @default:
	       */
		public static final String password = "password";
		/*
	       * @name: tables
	       * @description: tables to export data, format can support simple regex, table[0-63]
	       * @range: 
	       * @mandatory: true
	       * @default: 
	       */
		public static final String tables = "tables";
		/*
	       * @name: where
	       * @description: where clause, like 'modified_time > sysdate'
	       * @range: 
	       * @mandatory: false
	       * @default: 
	       */
		public static final String where = "where";
		/*
	       * @name: sql
	       * @description: self-defined sql statement
	       * @range: 
	       * @mandatory: false
	       * @default: 
	       */
		public static final String sql = "sql";
		/*
	       * @name: columns
	       * @description: columns to be selected, default is *
	       * @range: 
	       * @mandatory: false
	       * @default: *
	       */
		public static final String columns = "columns";
		/*
	       * @name: encoding
	       * @description: mysql database's encode
	       * @range: UTF-8|GBK|GB2312
	       * @mandatory: false
	       * @default: UTF-8
	       */
		public static final String encoding = "encoding";
		
       /*
	       * @name: mysql.params
	       * @description: mysql driver params, starts with no &, e.g. loginTimeOut=3000&yearIsDateType=false
	       * @range: 
	       * @mandatory: false
	       * @default:
	       */
		public static final String mysqlParams = "mysql.params";
		
		 /*
	       * @name: concurrency
	       * @description: concurrency of the job
	       * @range: 1-10
	       * @mandatory: false
	       * @default: 1
	       */
		public static final String concurrency = "concurrency";
		
		public static final String connurl = "connurl";
		
}
