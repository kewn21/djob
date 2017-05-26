/**
 * This file created at 2014-3-12.
 *

 */
package org.kesy.djob.dex.utils;

/**
 * <code>{@link IJdbcUrl}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public interface IJdbcUrl {

	public String getHostName();
	
	public String getPort();
	
	public String getDatabaseName();
	
	public String getJdbcType();
}
