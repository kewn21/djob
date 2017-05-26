
package org.kesy.djob.dex.datax.common.plugin;

import java.util.List;
import org.kesy.djob.dex.datax.common.exception.DataExchangeException;


/**
 * Split DataX job into sub-jobs. Users should implements the split rules.
 * 
 * @see Reader
 * @see Writer
 * 
 * */
public abstract class Splitter extends AbstractPlugin {
	
	/**
	 * Do Initialize work before split job.
	 * 
	 * @return 
	 * 			0 for OK, others for failure.
	 * 
	 * */
	public abstract int init();
	
	/**
	 * Split job into sub-jobs.
	 * 
	 * @return 
	 * 			a list of {@link PluginParam}, each for sub-jobs.
	 * 
	 * @throws DataExchangeException
	 * 			if split job failed.
	 * 
	 * */
	public abstract List<PluginParam> split();
}
