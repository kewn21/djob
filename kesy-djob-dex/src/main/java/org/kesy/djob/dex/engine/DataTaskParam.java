/**
 * This file created at 2014-3-4.
 *
 */
package org.kesy.djob.dex.engine;

import java.util.List;

import org.kesy.djob.dex.param.DataSourceParam;


/**
 * <code>{@link DataTaskParam}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public interface DataTaskParam {
	
	public DataSourceParam getReader();
	
    @SuppressWarnings("rawtypes")
    
	public List getWrites();
    
    public String getJobid();
}
