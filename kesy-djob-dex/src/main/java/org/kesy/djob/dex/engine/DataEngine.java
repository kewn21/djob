/**
 * This file created at 2014-2-26.
 *
 */
package org.kesy.djob.dex.engine;

import org.kesy.djob.dex.line.LineFactory;


/**
 * <code>{@link DataEngine}</code>
 *
 * TODO : document me
 *
 * @author pengyq
 */
public interface DataEngine {
	
	public void run(final DataTaskParam param, final DataEngineListener listener);
	
	public DataEngineResult run(final DataTaskParam param);
	
	public void finish();
	
	public void stop();
	
	public String getWorkProgress();
	
	public DataEngine setReaderLineFactory(LineFactory lineFactory);
	
	public DataEngine setWriterLineFactory(LineFactory lineFactory);

}
