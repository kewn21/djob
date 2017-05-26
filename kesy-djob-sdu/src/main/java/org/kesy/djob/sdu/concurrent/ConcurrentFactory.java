/**
 * 2014年8月26日
 */
package org.kesy.djob.sdu.concurrent;

import org.kesy.djob.core.spring.SpringFactory;

/**
 * @author kewn
 *
 */
public abstract class ConcurrentFactory {
	
	public static ConcurrentFactory get() {
		return SpringFactory.getBean("concurrentFactory", ConcurrentFactory.class);
	}
	
	public abstract boolean execute(TaskRunner runner) throws Exception;
	
	public abstract void stop(String id) throws Exception;
	
	public abstract void next(String id) throws Exception;

}
