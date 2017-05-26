/**
 * This file created at 2014-3-10.
 *
 */
package org.kesy.djob.dex.config;


/**
 * <code>{@link DataexEngineConfig}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class DataexEngineConfig {
	private String name;
	private Boolean active;
	private String clazz;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
	/**
	 * @return the clazz
	 */
	public String getClazz() {
		return clazz;
	}
	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	

}
