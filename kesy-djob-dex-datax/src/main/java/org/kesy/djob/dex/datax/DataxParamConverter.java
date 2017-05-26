/**
 * This file created at 2014-3-18.
 *

 */
package org.kesy.djob.dex.datax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kesy.djob.dex.DataexParamConverter;
import org.kesy.djob.dex.datax.param.DataxMapParam;
import org.kesy.djob.dex.datax.param.DataxParam;
import org.kesy.djob.dex.engine.DataTaskParam;
import org.kesy.djob.dex.param.DataSourceParam;
import org.kesy.djob.dex.param.DataTargetParam;
import org.kesy.djob.dex.param.PluginName;

/**
 * <code>{@link DataxParamConverter}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class DataxParamConverter implements DataexParamConverter {

	/* (non-Javadoc)
	 * @see org.kesy.djob.dex.core.DataexParamConverter#getParam(java.lang.String, org.kesy.djob.dex.core.model.PluginName, java.util.Map, org.kesy.djob.dex.core.model.PluginName, java.util.Map)
	 */
	@Override
	public DataTaskParam getParam(String jobId, PluginName sourceType, Map<String, String> sourceParam
			, PluginName targetType, Map<String, String> targetParam) {
		
		List<DataTargetParam> tParams = new ArrayList<DataTargetParam>();
		DataSourceParam sParam = new DataxMapParam(sourceParam, sourceType);
		DataTargetParam tParam = new DataxMapParam(targetParam, targetType);
		tParams.add(tParam);
		
		return new DataxParam(jobId, sParam, tParams);
	}
	
}
