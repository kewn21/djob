/**
 * This file created at 2014-2-26.
 *
 */
package org.kesy.djob.dex.engine;

/**
 * <code>{@link DataEngineStatus}</code>
 *
 * TODO : document me
 *
 * @author kewn
 * @desc define status for dataex engine
 */
public class DataEngineStatus {
	
	/**
	 * engine have not been inited
	 */
	public static final int NOT_READY = 0;
	
	
	/**
	 * engine have  been inited waiting for work
	 */	
	public static final int READY = 1;
	
	
	/**
	 * engine is working
	 */	
	public static final int WORKING = 2;
	
	
	/**
	 * engine  Finished work
	 */	
	public static final int FINISHED = 3;
	
	
	/**
	 * engine  not finish work,but been Interrupt
	 */	
	public static final int INTERRUPTED = 4;

}
