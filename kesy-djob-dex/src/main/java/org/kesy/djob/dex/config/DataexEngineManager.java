/**
 * This file created at 2014-3-10.
 *
 */
package org.kesy.djob.dex.config;

import java.io.IOException;
import java.util.List;

import org.kesy.djob.core.json.Xml;
import org.kesy.djob.dex.consts.ConfConsts;
import org.kesy.djob.dex.engine.DataEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>{@link DataexEngineManager}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class DataexEngineManager {
	private static final Logger logger = LoggerFactory.getLogger(DataexEngineManager.class);
	
	private static final String confPath = ConfConsts.CONF_ROOT_PATH + "dataex-engine.xml";
	private static List<DataexEngineConfig> dataexConfigs;

	private static final String LOAD_DATAEX_EXCEPTION = "load DataexEngine failed";
	private static final String NO_FOUND_DATAEX_EXCEPTION = "no found DataexEngine %s";
	private static final String INSTANCE_DATAEX_EXCEPTION = "instance DataexEngine %s error : \n\t%s";
	private static final String DEFAULT_ENGINE = "default";

	static {
		try {
			dataexConfigs = Xml.toList4jar(DataexEngineConfig.class, confPath);
		} catch (IOException e) {
			logger.error(LOAD_DATAEX_EXCEPTION);
			throw new RuntimeException(LOAD_DATAEX_EXCEPTION);
		}
	}

	private DataexEngineManager() {
	}

	public static DataexEngineConfig getDataexConfig() {
		DataexEngineConfig config = null;
		if (dataexConfigs == null || dataexConfigs.size() == 0) {
			String exception_msg = String.format(NO_FOUND_DATAEX_EXCEPTION, "");
			logger.error(exception_msg);
			throw new RuntimeException(exception_msg);
		}

		for (DataexEngineConfig triggerConfig : dataexConfigs) {
			if (triggerConfig.getActive()) {
				config = triggerConfig;
				break;
			}
		}

		if (config == null) {
			String exception_msg = String.format(NO_FOUND_DATAEX_EXCEPTION, DEFAULT_ENGINE);
			logger.error(exception_msg);
			throw new RuntimeException(exception_msg);
		}
		return config;
	}

	public static DataexEngineConfig getDataexConfig(String dataexName) {
		if (dataexConfigs == null || dataexConfigs.size() == 0) {
			String exception_msg = String.format(NO_FOUND_DATAEX_EXCEPTION,
					dataexName);
			logger.error(exception_msg);
			throw new RuntimeException(exception_msg);
		}

		DataexEngineConfig config = null;
		for (DataexEngineConfig triggerConfig : dataexConfigs) {
			if (dataexName.equals(triggerConfig.getName())) {
				config = triggerConfig;
				break;
			}
		}

		if (config == null) {
			String exception_msg = String.format(NO_FOUND_DATAEX_EXCEPTION,
					dataexName);
			logger.error(exception_msg);
			throw new RuntimeException(exception_msg);
		}
		return config;

	}

	public static DataEngine getEngine(String dataexName) {
		DataEngine engine;
		DataexEngineConfig config = getDataexConfig(dataexName);
		try {
			Class<?> clazz = Class.forName(config.getClazz());
			engine = (DataEngine) clazz.newInstance();
			return engine;
		} catch (Exception e) {
			String exception_msg = String.format(INSTANCE_DATAEX_EXCEPTION, 
					dataexName,  e);
			logger.error(exception_msg);
		}
		
		return null;
	}

	public static DataEngine getEngine() {
		DataexEngineConfig config = getDataexConfig();
		DataEngine engine;
		try {
			Class<?> clazz = Class.forName(config.getClazz());
			engine = (DataEngine) clazz.newInstance();
			return engine;
		} catch (Exception e) {
			String exception_msg = String.format(INSTANCE_DATAEX_EXCEPTION, 
					config.getName(),  e);
			logger.error(exception_msg);
		}
		
		return null;
	}
}
