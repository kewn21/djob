/**
 * This file created at 2014-3-6.
 *

 */
package org.kesy.djob.dex;

import org.kesy.djob.dex.engine.DataEngineListener;
import org.kesy.djob.dex.engine.DataTaskParam;

/**
 * <code>{@link DataexManager}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public interface DataexManager {
	
	public void extract(final DataTaskParam param, final DataEngineListener listener) throws RuntimeException;
	
	/*public DataexManager setFileName(String fileName);
	public DataexManager setDisplayName(String displayName);
	public DataexManager setFolderName(String folderName);
	public DataexManager setDescription(String description);
	public DataexManager setPrestorage(String prestorage);
	public DataexManager setPrestorageFileName(String prestorageFileName);
	public DataexManager setCreator(String creator);*/
	
}
