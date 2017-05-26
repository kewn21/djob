/**
 * This file created at 2014-1-7.
 *
 * Copyright (c) 2002-2014 Bingosoft, Inc. All rights reserved.
 */
package org.kesy.djob.dex.ds;

import java.io.IOException;
import java.util.List;

import org.kesy.djob.core.json.Xml;




/**
 * <code>{@link DatasourceManager}</code>
 *
 * TODO : document me
 *
 * @author dengqb
 */
public class DatasourceManager {
	
	private static List<DatasourceConfig> datasourceConfigs;
	
	private static final String				DEFAULT_FILE_PATH	= "/dex/datasource-conf.xml";
	private static final String				LOAD_STORAGE_FAILED	= "load datasource failed";
	
	static{
		try {
			datasourceConfigs = Xml.toList4jar(DatasourceConfig.class, DEFAULT_FILE_PATH);
		} catch (IOException e) {
			throw new RuntimeException(LOAD_STORAGE_FAILED);
		}
	}
	
	private DatasourceManager() {
	}
	
	public static List<DatasourceConfig> getDatasources() throws RuntimeException {
		return datasourceConfigs;
	}
	
	public static DatasourceConfig getDatasource(String datasource) throws RuntimeException {
		if (datasourceConfigs != null && datasourceConfigs.size() > 0) {
			for (DatasourceConfig config : datasourceConfigs) {
				if (datasource.equals(config.getName())) {
					return config;
				}
			}
		}
		return null;
	}
	
	public synchronized static boolean existDatasource(String datasource){
		for (DatasourceConfig config : datasourceConfigs) {
			if (datasource.equals(config.getName())) {
				return true;
			}
		}
		return false;
	}
}
