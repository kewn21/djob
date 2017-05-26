
package org.kesy.djob.dex.datax.common.plugin;

import org.kesy.djob.dex.line.Line;
import org.kesy.djob.dex.datax.engine.plugin.BufferedLineExchanger;
import org.kesy.djob.dex.datax.engine.storage.Storage;

/**
 * DataX use {@link Storage} to help {@link Reader} and {@link Writer} to exchange data,
 * {@link Writer} uses {@link LineReceiver} to get data from s{@link Storage}(Usually in memory).
 *  
 * @see LineSender
 * @see BufferedLineExchanger
 * 
 * */
public interface LineReceiver {
	
	/**
	 * Fetch the next {@link Line} from {@link Storage}.
	 * 
	 * @return	{@link Line}
	 * 			next {@link Line} in {@link Storage}, null if read to end.
	 * 
	 * */
	public Line getFromReader();
}
