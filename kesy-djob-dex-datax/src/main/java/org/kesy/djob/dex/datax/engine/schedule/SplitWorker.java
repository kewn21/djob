
package org.kesy.djob.dex.datax.engine.schedule;

import java.lang.reflect.Method;
import java.util.List;

import org.kesy.djob.dex.datax.common.exception.DataExchangeException;
import org.kesy.djob.dex.datax.common.plugin.PluginParam;
import org.kesy.djob.dex.datax.common.plugin.Splitter;
import org.kesy.djob.dex.datax.engine.conf.PluginConf;


/**
 * {@link SplitWorker} represents executor of {@link Splitter}
 * {@link Engine} use {@link SplitWorker} to split job.
 * 
 * */
public class SplitWorker extends PluginWorker {
	private Method init;

	private Method split;

	/**
	 * Constructor for {@link SplitWorker}
	 * 
	 * @param		pluginConf	
	 * 			@see {@link PluginConf}
	 * 
	 * @param		myClass
	 * 			class of {@link Splitter}
	 * 
	 * @throw {@link DataExchangeException}
	 *  
	 * */
	public SplitWorker(PluginConf pluginConf, Class<?> myClass) {
		super(pluginConf, myClass);
		try {
			init = myClass.getMethod("init", new Class[] {});
			split = myClass.getMethod("split", new Class[] {});
		}  catch (Exception e) {
			throw new DataExchangeException(e.getCause());
		}
	}

	/**
	 * Split job into sub-jobs </br>
	 *
	 * @return
	 * 			a list of sub-jobs params
	 * 
	 * @throws	{@link DataExchangeException}
	 *  
	 * */
	@SuppressWarnings("unchecked")
	public List<PluginParam> doSplit() {
		try {
			int iRetCode = (Integer) init.invoke(myObject, new Object[] {});
			if (iRetCode != 0)
				throw new DataExchangeException("Splitter split job failed .");
			return (List<PluginParam>) split.invoke(myObject, new Object[] {});
		} catch (Exception e) {
			throw new DataExchangeException(e.getCause());
		}
	}
}
