/**
 * This file created at 2014-2-27.
 *
 */
package org.kesy.djob.dex.param;

/**
 * <code>{@link PluginName}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public enum PluginName {
	ORACLE_READER("OracleReader"),
	ORACLE_WRITER("OracleWriter"),
	MYSQL_READER("MySqlReader"),
	MYSQL_WRITER("MySqlWriter"),
	SQLSERVER_READER("SqlServerReader"),	
	STREAM_READER("StreamReader"),
	STREAM_WRITER("StreamWriter"),
	FTP_WRITER("FtpWriter"),
	HIVE_WRITER("HiveReader"),
	DB2_WRITER("DB2Reader"),
	HDFS_WRITER("HdfsWriter");
	
	private final String name;

	private PluginName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static final PluginName nameOf(String name) {
		for (PluginName plugin : values()) {
			if (plugin.name.equals(name)) {
				return plugin;
			}
		}
		return PluginName.MYSQL_READER;
	}
}
