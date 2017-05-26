/**
 * This file created at 2014-3-17.
 *

 */
package org.kesy.djob.dex;

import java.util.Map;
import java.util.UUID;

import org.kesy.djob.dex.config.DataexEngineConfig;
import org.kesy.djob.dex.config.DataexEngineManager;
import org.kesy.djob.dex.config.DataexParamConverterManager;
import org.kesy.djob.dex.engine.DataTaskParam;
import org.kesy.djob.dex.param.PluginName;

/**
 * <code>{@link DataexParamFactory}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class DataexParamFactory {

	public static DataTaskParam getParam(PluginName sourceType, Map<String, String> sourceParam
			, PluginName targetType, Map<String, String> targetParam) {
		return getParam(UUID.randomUUID().toString(), sourceType, sourceParam, targetType, targetParam);
	}
	
	public static DataTaskParam getParam(String jobId, PluginName sourceType, Map<String, String> sourceParam
			, PluginName targetType, Map<String, String> targetParam) {
		
		DataexEngineConfig engineConfig = DataexEngineManager.getDataexConfig();
		DataexParamConverter paramConverter = DataexParamConverterManager
											.getConverter(engineConfig.getName());
		
		if (paramConverter != null) {
			return paramConverter.getParam(jobId, sourceType, sourceParam, targetType, targetParam);
		}
		else {
			throw new RuntimeException(String.format("no found paramConverter of engine [%s]", engineConfig.getName()));
		}
	}
}
