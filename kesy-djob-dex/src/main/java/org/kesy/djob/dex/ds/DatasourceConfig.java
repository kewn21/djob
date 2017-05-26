/**
 * This file created at 2014-1-16.
 *
 * Copyright (c) 2002-2014 Bingosoft, Inc. All rights reserved.
 */
package org.kesy.djob.dex.ds;

import java.util.HashMap;
import java.util.Map;

/**
 * <code>{@link DatasourceConfig}</code>
 *
 * TODO : document me
 *
 * @author dengqb
 */
public class DatasourceConfig {

	private String name;
	private String type;
	private Map<String, String> params;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public void addParam(String key, String value){
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(key, value);
	}
	
}
