/**
 * 2014年8月26日
 */
package org.kesy.djob.sdu.concurrent;

/**
 * @author kewn
 *
 */
public interface TaskRunner {
	
	String getTaskId();
	
	void hold() throws Exception;
	
	void start() throws Exception;
	
	void stop() throws Exception;
	
}
