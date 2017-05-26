/**
 * 2014年9月2日
 */
package org.kesy.djob.sdu.api.cluster;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author kewn
 *
 */
public class ClusterInfo {
	
	public static String ip;
	public static int port;
	
	public static String getNodeInfo() {
		return String.format("%s:%s", getIp() , port);
	}
	
	public static String getIp() {
		try {
			return ip != null ? ip : InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

}
