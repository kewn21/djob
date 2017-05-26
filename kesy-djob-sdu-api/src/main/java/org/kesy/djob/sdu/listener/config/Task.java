/**
 * This file created at Jan 13, 2014.
 *
 */
package org.kesy.djob.sdu.listener.config;

/**
 * 类<code>{@link Task}</code>  创建于 Jan 13, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class Task {

	private String name;
	private String clazz;
	private String listeners;

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

	public String getListeners() {
		return listeners;
	}

	public void setListeners(String listeners) {
		this.listeners = listeners;
	}
}
