/**
 * This file created at Jan 13, 2014.
 *
 */
package org.kesy.djob.sdu.listener.config;

import java.util.Map;

/**
 * 类<code>{@link Listener}</code>  创建于 Jan 13, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class Listener {

	private String name;
	private String clazz;
	private Map<String, String> params;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getParams() {
		return params;
	}

}
