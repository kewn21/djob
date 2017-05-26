
package org.kesy.djob.dex.datax.engine.conf;

/**
 * Configuration of plugins.
 * 
 * */
public class PluginConf {

	private String version;

	private String name;

	private String type;

	private String target;

	private String className;

	private int maxthreadnum;
	
	/**
	 * Get class name of plugin.
	 * 
	 * @return
	 * 			class name of plugin.
	 * 
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set class name of plugin.
	 * 
	 * @param	className
	 * 			class name of plugin.
	 * 
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Get version of plugin.
	 * 
	 * @return
	 * 			version of plugin.
	 * 
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set version of plugin.
	 * 
	 * @param	version
	 * 			version of plugin.
	 * 
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Get name of plugin.
	 * 
	 * @return
	 * 			name of plugin.
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of plugin.
	 * 
	 * @param	name
	 * 			name of plugin.
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get type of plugin.
	 * 
	 * @return
	 * 			type of plugin.
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set type of plugin.
	 * 
	 * @param	type
	 * 			type of plugin.
	 * 
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get target of plugin.
	 * 
	 * @return
	 * 			target of plugin
	 * 
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Set target of plugin.
	 * 
	 * @param	target
	 * 			target of plugin.
	 *
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Get max thread amount of plugin.
	 * 
	 * @return
	 * 			max thread amount of plugin.
	 */
	public int getMaxthreadnum() {
		return maxthreadnum;
	}

	/**
	 * Set max thread amount of plugin.
	 * 
	 * @param	maxthreadnum
	 * 			max thread amount of plugin.
	 * 
	 */
	public void setMaxthreadnum(int maxthreadnum) {
		this.maxthreadnum = maxthreadnum;
	}

}
