
package org.kesy.djob.dex.datax.engine.storage;

import org.kesy.djob.dex.datax.common.exception.DataExchangeException;

/**
 * As its name, This class produce storage space.
 * 
 */
public class StorageFactory {
	private StorageFactory(){
	}
	
	/**
	 * Produce storage space according to the given classname.
	 * 
	 * @param	className
	 * 			Full Name of A concrete storage. 
	 * @return 
	 * 			If nothing wrong, return a storage, else return null.
	 * 
	 */
	public static Storage product(String className) {
		try {
			return (Storage) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new DataExchangeException(e.getCause());
		}
	}
}
