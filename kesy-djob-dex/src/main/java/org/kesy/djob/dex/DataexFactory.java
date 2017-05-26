/**
 * This file created at 2014-2-26.
 *

 */
package org.kesy.djob.dex;

import org.kesy.djob.dex.config.DataexEngineManager;
import org.kesy.djob.dex.engine.DataEngine;

/**
 * <code>{@link DataexFactory}</code>
 * 
 * TODO : document me
 * 
 * @author pengyq
 */
public class DataexFactory {
	
	public static DataEngine getEngine() throws Exception {
		return DataexEngineManager.getEngine();
	}

	public static DataEngine getEngine(String engineName) throws Exception {
		return DataexEngineManager.getEngine(engineName);
	}
	
}
