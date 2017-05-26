/**
 * This file created at 2014-3-20.
 *

 */
package org.kesy.djob.dex.datax.engine.conf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kesy.djob.dex.datax.common.consts.ConfigConsts;

/**
 * <code>{@link PluginPropertyConf}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class PluginPropertyConf {
	
	private static final Log logger = LogFactory.getLog(PluginPropertyConf.class);
	private static final String PLUGIN_CONF_FILE_PATH = ConfigConsts.CONF_ROOT_PATH + "/datax/plugin-conf.properties";
	private static Properties properties = new Properties();
	
	static {
		try {
			properties
			.load(PluginPropertyConf.class
					.getResourceAsStream(PLUGIN_CONF_FILE_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error("Can not find file:" + PLUGIN_CONF_FILE_PATH);
			logger.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("read file:" + PLUGIN_CONF_FILE_PATH + " error");
			logger.error(e);
		}
	}
	
	
	public static int getJdbcInitialSize() {
		return Integer.valueOf(properties.getProperty("plugin.jdbc.initialSize"));
	}

	public static int getJdbcMaxActive() {
		return Integer.valueOf(properties.getProperty("plugin.jdbc.maxActive"));
	}

	public static int getJdbcMaxIdle() {
		return Integer.valueOf(properties.getProperty("plugin.jdbc.maxIdle"));
	}

	public static int getJdbcMinIdle() {
		return Integer.valueOf(properties.getProperty("plugin.jdbc.minIdle"));
	}
}
