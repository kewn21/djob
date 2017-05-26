/**
 * This file created at 2014-2-26.
 *

 */
package org.kesy.djob.dex.datax;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.kesy.djob.dex.datax.common.exception.ExceptionTracker;
import org.kesy.djob.dex.datax.common.plugin.PluginParam;
import org.kesy.djob.dex.datax.common.utils.ObjectUtils;
import org.kesy.djob.dex.datax.engine.conf.EngineConf;
import org.kesy.djob.dex.datax.engine.conf.JobConf;
import org.kesy.djob.dex.datax.engine.conf.JobPluginConf;
import org.kesy.djob.dex.datax.engine.conf.ParseXMLUtil;
import org.kesy.djob.dex.datax.engine.conf.PluginConf;
import org.kesy.djob.dex.datax.engine.plugin.DefaultPluginParam;
import org.kesy.djob.dex.datax.engine.schedule.Engine;
import org.kesy.djob.dex.datax.engine.storage.Storage.Statistics;
import org.kesy.djob.dex.datax.param.IDataxParam;
import org.kesy.djob.dex.engine.DataEngine;
import org.kesy.djob.dex.engine.DataEngineListener;
import org.kesy.djob.dex.engine.DataEngineResult;
import org.kesy.djob.dex.engine.DataTaskParam;
import org.kesy.djob.dex.line.LineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>{@link DataxDataexEngine}</code>
 * 
 * TODO : document me
 * 
 * @author pengyq
 */
public class DataxDataexEngine implements DataEngine {

	private static final Logger logger = LoggerFactory
			.getLogger(DataxDataexEngine.class);

	private DataEngineListener listener = null;
	private LineFactory readerLineFactory = null;
	private LineFactory writerLineFactory = null;


	/**
	 * @return the listener
	 */
	public DataEngineListener getListener() {
		return listener;
	}

	private static void confLog(String jobId) {
		/*
		 * java.util.Calendar c = java.util.Calendar.getInstance();
		 * java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(
		 * "yyyy-MM-dd"); String logDir =
		 * URLDecoder.decode(DataxDataexEngine.class.getResource(
		 * confFolder).getPath() + "/logs/" + f.format(c.getTime()));
		 * System.setProperty("log.dir", logDir); f = new
		 * java.text.SimpleDateFormat("HHmmss"); String logFile = jobId + "." +
		 * f.format(c.getTime()) + ".log"; System.setProperty("log.file",
		 * logFile); PropertyConfigurator.configure(URLDecoder
		 * .decode(DataxDataexEngine.class.getResource(confFolder) .getPath() +
		 * "/log4j.properties"));
		 */
	}

	private JobConf createJobConf(DataTaskParam param) {
		IDataxParam reader = (IDataxParam) param.getReader();
		
		logger.info(String.format(
								"************************reader %s param*********************************",
								reader.getPluginId()));
		
		Map<String, String> readerParamMap = (reader.getParam() == null || reader
				.getParam().size() < 1) ? ObjectUtils.getFieldValuePair(reader)
				: reader.getParam();
				
		List<IDataxParam> writes = param.getWrites();

		JobConf job = new JobConf();
		job.setId(param.getJobid() == null ? "DataX_is_still_a_virgin" : param
				.getJobid().trim());

		JobPluginConf readerJobConf = new JobPluginConf();
		String readerId = reader.getPluginId().getName();
		readerJobConf.setId(readerId == null ? "virgin-reader" : readerId);
		readerJobConf.setName(readerId.replace("loader", "reader")
				.toLowerCase());
		/*
		 * for historic reason, we need to check concurrency node first add by
		 * bazhen.csy
		 */
		if (!readerParamMap.containsKey("concurrency")) {
			readerParamMap.put("concurrency", reader.getConcurrency());
		}

		PluginParam readerPluginParam = new DefaultPluginParam(readerParamMap);

		// if (!readerPluginParam.hasValue("concurrency")
		// || readerPluginParam.getIntValue("concurrency", 1) < 0) {
		// throw new IllegalArgumentException(
		// "Reader option [concurrency] error !");
		// }

		readerJobConf.setPluginParams(readerPluginParam);

		List<JobPluginConf> writerJobConfs = new ArrayList<JobPluginConf>();
		for (IDataxParam write : writes) {
			JobPluginConf writerPluginConf = new JobPluginConf();

			String writerId = write.getPluginId().getName();
			writerPluginConf.setId(writerId == null ? "virgin-writer"
					: writerId.trim());

			/*
			 * String destructLimit = write.attributeValue("destructlimit"); if
			 * (destructLimit != null) {
			 * writerPluginConf.setDestructLimit(Integer
			 * .valueOf(destructLimit)); }
			 */

			writerPluginConf.setName(writerId.replace("dumper", "writer")
					.toLowerCase());
			
			logger.info(String.format(
									"************************writer %s param*********************************",
									writerId));
			
			Map<String, String> writerParamMap = (write.getParam() == null || write
					.getParam().size() < 1) ? ObjectUtils
					.getFieldValuePair(write) : write.getParam();

			/*
			 * for historic reason, we need to check concurrency node add by
			 * bazhen.csy
			 */
			// writerParamMap.put("concurrency", write.getConcurrency());

			PluginParam writerPluginParam = new DefaultPluginParam(
					writerParamMap);

			writerPluginConf.setPluginParams(writerPluginParam);
			writerJobConfs.add(writerPluginConf);
		}
		job.setReaderConf(readerJobConf);
		job.setWriterConfs(writerJobConfs);
		return job;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.kesy.djob.dex.core.DataexEngine#run(org.kesy.djob.dex.core.
	 * model.DataexParam, org.kesy.djob.dex.core.DataexTaskListener)
	 */
	public void run(final DataTaskParam param, final DataEngineListener listener) {
		
		DataEngineResult result = run(param);
		
		listener.notify(result);
	}
	
	private DataEngineResult getTotalRes(List<Statistics> statistics, DataEngineResult res) {
		res.setEndTime(new Date());
		long lineR=0;
		long byteRxTotal=0;
		long totalLine=0;
		String[] lineCounts;
		for (Statistics stat : statistics) {
			lineCounts = stat.getStorage().info().split(":");
			lineR += Long.parseLong(lineCounts[0]);
			totalLine += Long.parseLong(lineCounts[1]);
			byteRxTotal += stat.getByteRxTotal();
			totalLine += stat.getLineTx();
		}
		res.setTransferedLine(lineR);
		res.setByteCount(byteRxTotal);
		res.setTotalPlanLine(totalLine);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.core.DataexEngine#finish()
	 */
	public void finish() {
		// TODO implement DataexEngine.finish
		logger.info("Engine work finished......");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.core.DataexEngine#getWorkProgress()
	 */
	public String getWorkProgress() {
		// TODO implement DataexEngine.getWorkProgress
		logger.info("Engine work progress......");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kesy.djob.dex.core.DataexEngine#stop()
	 */
	public void stop() {
		// TODO implement DataexEngine.stop
		logger.info("Engine work stopped......");

	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.dex.core.engine.DataEngine#setReaderLineFactory(org.kesy.djob.core.data.LineFactory)
	 */
	@Override
	public DataEngine setReaderLineFactory(LineFactory lineFactory) {
		this.readerLineFactory = lineFactory;
		return this;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.dex.core.engine.DataEngine#setWriterLineFactory(org.kesy.djob.core.data.LineFactory)
	 */
	@Override
	public DataEngine setWriterLineFactory(LineFactory lineFactory) {
		this.writerLineFactory = lineFactory;
		return this;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.dex.engine.DataEngine#run(org.kesy.djob.dex.engine.DataTaskParam)
	 */
	@Override
	public DataEngineResult run(DataTaskParam param) {
		
		logger.info("Engine begin running......");
		
		if (param.getReader() == null) {
			DataEngineResult result = new DataEngineResult();
			result
					.setException("Engine Param mast been init,the reader param of DataexTaskParam is null......");
			logger
					.error("Engine Param mast been init,the reader param of DataexTaskParam is null......");
			return result;
		}
		if (param.getWrites() == null || param.getWrites().size() < 1) {
			DataEngineResult result = new DataEngineResult();
			result
					.setException("Engine Param mast been init,the write param of DataexTaskParam is empty......");
			logger
					.error("Engine Param mast been init,the write param of DataexTaskParam is empty......");
			return result;
		}
		
		// TODO implement DataexEngine.run
		// String jobDescFile = null;
		/*
		 * if (args.length < 1) {
		 * System.exit(JobConfGenDriver.produceXmlConf()); } else if
		 * (args.length == 1) { jobDescFile = args[0]; } else {
		 * System.out.printf("Usage: java -jar engine.jar job.xml .");
		 * System.exit(ExitStatus.FAILED.value()); }
		 */

		confLog("BEFORE_CHRIST");
		JobConf jobConf = createJobConf(param);
		confLog(jobConf.getId());
		EngineConf engineConf = ParseXMLUtil.loadEngineConfig();
		Map<String, PluginConf> pluginConfs = ParseXMLUtil.loadPluginConfig();
		

		DataEngineResult result = new DataEngineResult();
		Engine engine = new Engine(engineConf, pluginConfs, readerLineFactory, writerLineFactory);
		try {
			int retcode = engine.start(jobConf);
			 
			// get task run info
			if (retcode == 0) {
				getTotalRes(engine.getStatistics(), result);
				
				logger.info("\n**************************************************" +
						"\nTask total run statistic info is:" + result.getResInfo() +
						"\n**************************************************");
			}
		} catch (Exception e) {
			result.setException(ExceptionTracker.trace(e));
			logger.error(ExceptionTracker.trace(e));
		}
		
		return result;
	}
	
}
