/**
 * 2014年9月4日
 */
package org.kesy.djob.server;

/**
 * @author kewn
 *
 */
public final class JobServerFactory {
	
	private JobServerFactory() {}
	
	public static JobServer createServer() {
		return new JobServer();
	}

}
