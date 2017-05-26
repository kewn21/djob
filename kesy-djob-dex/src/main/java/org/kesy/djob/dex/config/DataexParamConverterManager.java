/**
 * This file created at 2014-3-24.
 *
 */
package org.kesy.djob.dex.config;

import java.io.IOException;
import java.util.List;

import org.kesy.djob.core.json.Xml;
import org.kesy.djob.dex.DataexParamConverter;
import org.kesy.djob.dex.consts.ConfConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>{@link DataexParamConverterManager}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class DataexParamConverterManager {
	
	private static final Logger logger = LoggerFactory.getLogger(DataexParamConverterManager.class);
	private static final String confPath = ConfConsts.CONF_ROOT_PATH + "dataex-param-converter.xml";
	private static List<DataexParamConverterConfig> converterConfigs;

	private static final String LOAD_DATAEX_EXCEPTION = "load DataexParamConverter failed";
	private static final String NO_FOUND_DATAEX_EXCEPTION = "no found DataexParamConverter %s";
	private static final String INSTANCE_DATAEX_EXCEPTION = "instance DataexParamConverter %s error : \n\t%s";

	static {
		try {
			converterConfigs = Xml.toList4jar(DataexParamConverterConfig.class, confPath);
		} catch (IOException e) {
			logger.error(LOAD_DATAEX_EXCEPTION);
			throw new RuntimeException(LOAD_DATAEX_EXCEPTION);
		}
	}

	private DataexParamConverterManager() {
	}

	public static DataexParamConverterConfig getConverterConfig(String converterName) {
		if (converterConfigs == null || converterConfigs.size() == 0) {
			String exception_msg = String.format(NO_FOUND_DATAEX_EXCEPTION,
					converterName);
			logger.error(exception_msg);
			throw new RuntimeException(exception_msg);
		}

		DataexParamConverterConfig config = null;
		for (DataexParamConverterConfig triggerConfig : converterConfigs) {
			if (converterName.equals(triggerConfig.getName())) {
				config = triggerConfig;
				break;
			}
		}

		if (config == null) {
			String exception_msg = String.format(NO_FOUND_DATAEX_EXCEPTION,
					converterName);
			logger.error(exception_msg);
			throw new RuntimeException(exception_msg);
		}
		return config;

	}

	public static DataexParamConverter getConverter(String converterName) {
		DataexParamConverter paramConverter;
		DataexParamConverterConfig config = getConverterConfig(converterName);
		try {
			Class<?> clazz = Class.forName(config.getClazz());
			paramConverter = (DataexParamConverter) clazz.newInstance();
			return paramConverter;
		} catch (Exception e) {
			String exception_msg = String.format(INSTANCE_DATAEX_EXCEPTION, 
					converterName,  e);
			logger.error(exception_msg);
		}
		
		return null;
	}

}
