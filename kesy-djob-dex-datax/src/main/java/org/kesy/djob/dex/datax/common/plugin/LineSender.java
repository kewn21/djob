
package org.kesy.djob.dex.datax.common.plugin;

import org.kesy.djob.dex.line.Line;
import org.kesy.djob.dex.datax.engine.plugin.BufferedLineExchanger;
import org.kesy.djob.dex.datax.engine.storage.Storage;

/**
 * DataX use {@link Storage} to help {@link Reader} and {@link Writer} to exchange data,s
 * {@link Reader} use {@link LineSender} to put data into {@link Storage}.
 * 
 *  @see LineReceiver
 *  @see BufferedLineExchanger
 *  
 * */
public interface LineSender {
	
	/**
	 * Construct one {@link Line} of data in {@link Storage} which will be used to exchange data.
	 * 
	 * @return
	 * 			a new {@link Line}.
	 * 
	 * */
	public Line createLine();
	
	/**
	 * Put one {@link Line} into {@link Storage}.
	 * 
	 * @param line	
	 * 			{@link Line} of data pushed into {@link Storage}.
	 * 
	 * @return
	 *			true for OK, false for failure.
	 *
	 * */
	public boolean sendToWriter(Line line);
	
	/**
	 * For test
	 * */
	public boolean fakeSendToWriter(int lineLength);
	
	/**
	 * Flush data in buffer (if exists) to {@link Storage}.
	 * 
	 * */
	public void flush();
}
