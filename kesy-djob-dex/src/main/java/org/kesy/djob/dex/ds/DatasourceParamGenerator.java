/**
 * 2014年9月2日
 */
package org.kesy.djob.dex.ds;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.kesy.djob.dex.DataexParamFactory;
import org.kesy.djob.dex.engine.DataTaskParam;
import org.kesy.djob.dex.param.PluginName;


/**
 * @author kewn
 *
 */
public class DatasourceParamGenerator {
	
	private PluginName sourcePluginName;
	private PluginName targetPluginName;
	
	private Map<String, String> sourceParamMap;
	private Map<String, String> targetParamMap;
	
	public DatasourceParamGenerator(String sourceName, String targetName){
		DatasourceConfig sourceConfig = DatasourceManager.getDatasource(sourceName);
		DatasourceConfig targetConfig = DatasourceManager.getDatasource(targetName);
		
		sourcePluginName = PluginName.nameOf(sourceConfig.getType() + "Reader");
		targetPluginName = PluginName.nameOf(targetConfig.getType() + "Writer");
		
		sourceParamMap = copy(sourceConfig.getParams());
		targetParamMap = copy(targetConfig.getParams());
	}
	
	public void addSourceParam(String key, String value){
		sourceParamMap.put(key, value);
	}
	
	public void addTargetParam(String key, String value){
		targetParamMap.put(key, value);
	}
	
	public DataTaskParam build(){
		return DataexParamFactory.getParam(sourcePluginName, sourceParamMap
				, targetPluginName, targetParamMap);
	}
	
	private Map<String, String> copy(Map<String, String> srcMap) {
		if (srcMap == null || srcMap.size() == 0) {
			return null;
		}
		
		Map<String, String> targetMap = new HashMap<String, String>();
		for (Entry<String, String> entry : srcMap.entrySet()) {
			targetMap.put(entry.getKey(), entry.getValue());
		}
		
		return targetMap;
	}

}
