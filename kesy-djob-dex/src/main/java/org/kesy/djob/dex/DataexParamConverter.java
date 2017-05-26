/**
 * This file created at 2014-3-18.
 *

 */
package org.kesy.djob.dex;

import java.util.Map;

import org.kesy.djob.dex.engine.DataTaskParam;
import org.kesy.djob.dex.param.PluginName;

/**
 * <code>{@link DataexParamConverter}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public interface DataexParamConverter {
	
	DataTaskParam getParam(String jobId, PluginName sourceType, Map<String, String> sourceParam
			, PluginName targetType, Map<String, String> targetParam);
}
