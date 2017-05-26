/**
 * This file created at 2014年5月16日.
 */
package org.kesy.djob.web;

import java.util.Map;

/**
 * <code>{@link DataRequest}</code>
 *
 * TODO : document me
 *
 * @author dengqb
 */
public class DataRequest {
	
	private String commandName;
	private Map<String, Object> params;
	private Map<String, Object> callbackParams;
	
	/**
	 * @return the commandName
	 */
	public String getCommandName() {
		return commandName;
	}
	/**
	 * @param commandName the commandName to set
	 */
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	/**
	 * @return the callbackParams
	 */
	public Map<String, Object> getCallbackParams() {
		return callbackParams;
	}
	/**
	 * @param callbackParams the callbackParams to set
	 */
	public void setCallbackParams(Map<String, Object> callbackParams) {
		this.callbackParams = callbackParams;
	}
	
}
