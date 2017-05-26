package org.kesy.djob.sdu.api;

import org.kesy.djob.core.spring.SpringFactory;


public class JobStoreFactory {
	
	private static JobStore jobStore = SpringFactory.getBean("jobStore", JobStore.class);
	
	public static JobStore get() {
		return jobStore;
	}

}
