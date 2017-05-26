/**
 * This file created at 2014-3-7.
 *

 */
package org.kesy.djob.dex;

import org.kesy.djob.core.spring.SpringFactory;


/**
 * TODO : document me
 *
 * @author kewn
 */
public class DataexManagerFactory {
	
	private static DataexManager manager = SpringFactory.getBean("dataexManager", DataexManager.class);
	
	public static DataexManager get(){
		return manager;
	}

}
