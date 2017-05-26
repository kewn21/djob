/**
 * This file created at 2014-3-5.
 *

 */
package org.kesy.djob.dex.datax.param;

import java.util.List;
import java.util.UUID;

import org.kesy.djob.dex.engine.DataTaskParam;
import org.kesy.djob.dex.param.DataSourceParam;
import org.kesy.djob.dex.param.DataTargetParam;

/**
 * <code>{@link DataxParam}</code>
 * 
 * TODO : document me
 * 
 * @author kewn
 */
public class DataxParam implements DataTaskParam {
	
	private String jobid = UUID.randomUUID().toString();
	private DataSourceParam reader;
	private List<DataTargetParam> writers;

	public DataxParam(String jobid, DataSourceParam reader,
			List<DataTargetParam> writers) {
		this.jobid = jobid;
		this.reader = reader;
		this.writers = writers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.core.model.DataexParam#getJobid()
	 */
	@Override
	public String getJobid() {
		return this.jobid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.core.model.DataexParam#getReader()
	 */
	@Override
	public DataSourceParam getReader() {
		return (DataSourceParam) reader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.core.model.DataexParam#getWrites()
	 */
	@Override
	public List<DataTargetParam> getWrites() {
		return writers;
	}

}
