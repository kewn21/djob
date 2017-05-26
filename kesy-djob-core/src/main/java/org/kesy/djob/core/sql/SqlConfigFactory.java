package org.kesy.djob.core.sql;

public final class SqlConfigFactory {
	
	private static final XmlSqlSource sqlSource = new XmlSqlSource();
	
	static {
		sqlSource.loadSqls();
	}

	public static String getSql(String key) {
		return sqlSource.getSql(key);
	}
}
