
package org.kesy.djob.dex.datax.engine.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.kesy.djob.dex.datax.common.exception.DataExchangeException;
import org.kesy.djob.dex.datax.common.plugin.PluginParam;
import org.kesy.djob.dex.datax.common.plugin.Pluginable;
import org.kesy.djob.dex.datax.common.plugin.Reader;
import org.kesy.djob.dex.datax.common.plugin.Writer;
import org.kesy.djob.dex.datax.engine.conf.EngineConf;
import org.kesy.djob.dex.datax.engine.conf.JobConf;
import org.kesy.djob.dex.datax.engine.conf.JobPluginConf;
import org.kesy.djob.dex.datax.engine.conf.PluginConf;
import org.kesy.djob.dex.datax.engine.plugin.BufferedLineExchanger;
import org.kesy.djob.dex.datax.engine.storage.Storage;
import org.kesy.djob.dex.datax.engine.storage.Storage.Statistics;
import org.kesy.djob.dex.datax.engine.storage.StoragePool;
import org.kesy.djob.dex.line.LineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Core class of DataX, schedule {@link Reader} & {@link Writer}.
 * 
 * */
public class Engine {
	
	private static final Logger logger = LoggerFactory.getLogger(Engine.class);

	private static final int PERIOD = 10;

	private static final int MAX_CONCURRENCY = 64;

	private EngineConf engineConf;

	private Map<String, PluginConf> pluginReg;

	private MonitorPool readerMonitorPool;

	private MonitorPool writerMonitorPool;
	
	private StoragePool storagePool;
	
	private LineFactory readerLineFactory;
	
	private LineFactory writerLineFactory;
	

	/**
	 * Constructor for {@link Engine}
	 * 
	 * @param engineConf
	 *            Configuration for {@link Engine}
	 * 
	 * @param pluginReg
	 *            Configurations for {@link Pluginable}
	 * 
	 * */
	public Engine(EngineConf engineConf, Map<String, PluginConf> pluginReg) {
		this(engineConf, pluginReg, null, null);
	}
	
	/**
	 * Constructor for {@link Engine}
	 * 
	 * @param engineConf
	 *            Configuration for {@link Engine}
	 * 
	 * @param pluginReg
	 *            Configurations for {@link Pluginable}
	 * 
	 * @param readerLineFactory
	 *            storage which reader create line
	 *            
	 * @param writerLineFactory
	 *            storage which writer create line
	 * 
	 * */
	public Engine(EngineConf engineConf, Map<String, PluginConf> pluginReg
			, LineFactory readerLineFactory, LineFactory writerLineFactory) {
		this.engineConf = engineConf;
		this.pluginReg = pluginReg;

		this.writerMonitorPool = new MonitorPool();
		this.readerMonitorPool = new MonitorPool();
		
		this.readerLineFactory = readerLineFactory;
		this.writerLineFactory = writerLineFactory;
	}

	/**
	 * Start a DataX job.
	 * 
	 * @param jobConf
	 *            Configuration for the DataX Job.
	 * 
	 * @return 0 for success, others for failure.
	 * 
	 * @throws Exception
	 * 
	 */
	public int start(JobConf jobConf)
			throws Exception {
		logger.info("***************engineConf***************");
		logger.info('\n' + engineConf.toString() + '\n');
		logger.info("***************jobConf******************");
		logger.info('\n' + jobConf.toString() + '\n');
		logger.info("DataX starting  ......");
		
		
		storagePool = new StoragePool(jobConf, engineConf, PERIOD);
		NamedThreadPoolExecutor readerPool = initReaderPool(jobConf,
				storagePool);
		List<NamedThreadPoolExecutor> writerPool = initWriterPool(jobConf,
				storagePool);

		logger.info("DataX starts to exchange data .");
		
		readerPool.shutdown();
		for (NamedThreadPoolExecutor dp : writerPool) {
			dp.shutdown();
		}

		int sleepCnt = 0;
		int retcode = 0;

		while (true) {
			/* check reader finish? */
			boolean readerFinish = readerPool.isTerminated();
			if (readerFinish) {
				storagePool.closeInput();
			}

			boolean writerAllFinish = true;

			NamedThreadPoolExecutor[] dps = writerPool
					.toArray(new NamedThreadPoolExecutor[0]);
			/* check each DumpPool */
			for (NamedThreadPoolExecutor dp : dps) {
				if (!readerFinish && dp.isTerminated()) {
					
					logger.error(String.format("DataX Writer %s failed .",
							dp.getName()));
					
					writerPool.remove(dp);
				} else if (!dp.isTerminated()) {
					writerAllFinish = false;
				}
			}

			if (readerFinish && writerAllFinish) {
				logger.info("DataX Reader post work begins .");
				readerPool.doPost();
				logger.info("DataX Reader post work ends .");

				logger.info("DataX Writers post work begins .");
				for (NamedThreadPoolExecutor dp : writerPool) {
					dp.getParam().setOppositeMetaData(
							readerPool.getParam().getMyMetaData());
					dp.doPost();
				}
				logger.info("DataX Writers post work ends .");
				break;
			} else if (!readerFinish && writerAllFinish) {
				
				logger.error("DataX Writers finished before reader finished."); 
				logger.error("DataX job failed.");
				 
				readerPool.shutdownNow();
				readerPool.awaitTermination(3, TimeUnit.SECONDS);
				break;
			}

			Thread.sleep(1000);
			sleepCnt++;

			if (sleepCnt % PERIOD == 0) {
				/* reader&writer count num of thread */
				StringBuilder sb = new StringBuilder();
				
				sb.append(String.format("ReaderPool %s: Active Threads %d .",
				readerPool.getName(), readerPool.getActiveCount()));
				
				logger.info(sb.toString());

				sb.setLength(0);
				for (NamedThreadPoolExecutor perWriterPool : writerPool) {
					
					sb.append(String.format(
					"WriterPool %s: Active Threads %d .",
					perWriterPool.getName(),
					perWriterPool.getActiveCount()));
					 
					logger.info(sb.toString());
					sb.setLength(0);
				}

				logger.info(storagePool.getPeriodState());
			}
		}

		/*StringBuilder sb = new StringBuilder();

		sb.append(storagePool.getTotalStat());
		long discardLine = writerMonitorPool.getDiscardLine();
		sb.append(String.format("%-26s: %19d\n", "Total discarded records",
				discardLine));

		logger.info(sb.toString());*/


		long total = -1;
		boolean writePartlyFailed = false;
		for (Storage s : storagePool.getStorageForReader()) {
			String[] lineCounts = s.info().split(":");
			long lineTx = Long.parseLong(lineCounts[1]);
			if (total != -1 && total != lineTx) {
				writePartlyFailed = true;
				logger.error("Writer partly failed, for " + total + "!="
						+ lineTx);
			}
			total = lineTx;
		}
		
		return writePartlyFailed ? 200 : retcode;
	}
	
	private NamedThreadPoolExecutor initReaderPool(JobConf jobConf,
			StoragePool sp) throws Exception {

		// 获取数据来源Reader类型
		JobPluginConf readerJobConf = jobConf.getReaderConf();

		PluginConf readerConf = pluginReg.get(readerJobConf.getName());
		logger.info("readerConf:" + readerConf);
		logger.info("readerJobConf.getName():" + readerJobConf.getName());

		//启动已加载
		Class<?> readerClass = null;
		try {
			readerClass = Class.forName(readerConf.getClassName());
		} catch (Exception e) {
			logger.error(String.format("DataX Reader [ %s ] doesn't be loaded.", 
					readerConf.getName()));
			throw new Exception(String.format("DataX Reader [ %s ] doesn't be loaded.", 
					readerConf.getName()));
		}

		ReaderWorker readerWorkerForPreAndPost = new ReaderWorker(readerConf,
				readerClass);
		PluginParam sparam = jobConf.getReaderConf().getPluginParams();

		readerWorkerForPreAndPost.setParam(sparam);
		readerWorkerForPreAndPost.init();

		logger.info("DataX Reader prepare work begins .");
		int code = readerWorkerForPreAndPost.prepare(sparam);
		if (code != 0) {
			throw new
				DataExchangeException("DataX Reader prepare work failed!");
		}
		logger.info("DataX Reader prepare work ends .");

		logger.info("DataX Reader split work begins .");
		List<PluginParam> readerSplitParams = readerWorkerForPreAndPost
				.doSplit(sparam);
		
		logger.info(String.format(
			"DataX Reader splits this job into %d sub-jobs",
			readerSplitParams.size()));
		 
		logger.info("DataX Reader split work ends .");

		int concurrency = readerJobConf.getConcurrency();
		if (concurrency <= 0 || concurrency > MAX_CONCURRENCY) {
			throw new IllegalArgumentException(String.format(
				"Reader concurrency set to be %d, make sure it must be between [%d, %d] ."
				, concurrency, 1, MAX_CONCURRENCY));
		}

		concurrency = Math.min(concurrency, readerSplitParams.size());
		if (concurrency <= 0) {
			concurrency = 1;
		}
		readerJobConf.setConcurrency(concurrency);

		NamedThreadPoolExecutor readerPool = new NamedThreadPoolExecutor(
				readerJobConf.getId(), readerJobConf.getConcurrency(),
				readerJobConf.getConcurrency(), 1L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());

		readerPool.setPostWorker(readerWorkerForPreAndPost);
		readerPool.setParam(sparam);

		readerPool.prestartAllCoreThreads();

		logger.info("DataX Reader starts to read data .");
		for (PluginParam param : readerSplitParams) {
			ReaderWorker readerWorker = new ReaderWorker(readerConf, readerClass);
			readerWorker.setParam(param);
			readerWorker.setLineSender(new BufferedLineExchanger(null, sp
					.getStorageForReader(), this.engineConf
					.getStorageBufferSize(), 
					this.readerLineFactory));
			readerPool.execute(readerWorker);
			readerMonitorPool.monitor(readerWorker);
		}

		return readerPool;
	}

	private List<NamedThreadPoolExecutor> initWriterPool(JobConf jobConf,
			StoragePool sp) throws Exception {
		List<NamedThreadPoolExecutor> writerPoolList = new ArrayList<NamedThreadPoolExecutor>();
		List<JobPluginConf> writerJobConfs = jobConf.getWriterConfs();
		for (JobPluginConf dpjc : writerJobConfs) {
			PluginConf writerConf = pluginReg.get(dpjc.getName());

			//启动已加载
			Class<?> myClass = null;
			try {
				myClass = Class.forName(writerConf.getClassName());
			} catch (Exception e) {
				logger.error(String.format("DataX Writer [ %s ] doesn't be loaded."
						, writerConf.getName()));
				throw new Exception(String.format("DataX Writer [ %s ] doesn't be loaded."
						, writerConf.getName()));
			}
			
			WriterWorker writerWorkerForPreAndPost = new WriterWorker(
					writerConf, myClass);

			PluginParam writerParam = dpjc.getPluginParams();
			writerWorkerForPreAndPost.setParam(writerParam);
			writerWorkerForPreAndPost.init();

			logger.info("DataX Writer prepare work begins .");
			int code = writerWorkerForPreAndPost.prepare(writerParam);
			if (code != 0) {
				throw new DataExchangeException(
					"DataX Writer prepare work failed!");
			}
			logger.info("DataX Writer prepare work ends .");

			logger.info("DataX Writer split work begins .");
			List<PluginParam> writerSplitParams = writerWorkerForPreAndPost
					.doSplit(writerParam);
			
			logger.info(String.format(
				"DataX Writer splits this job into %d sub-jobs .",
				writerSplitParams.size()));
			
			logger.info("DataX Writer split work ends .");

			int concurrency = dpjc.getConcurrency();
			if (concurrency <= 0 || concurrency > MAX_CONCURRENCY) {
				
				throw new IllegalArgumentException(String.format(
					"Writer concurrency set to be %d, make sure it must be between [%d, %d] ."
					, concurrency, 1, MAX_CONCURRENCY));
			}

			concurrency = Math.min(dpjc.getConcurrency(), writerSplitParams
					.size());
			if (concurrency <= 0) {
				concurrency = 1;
			}
			dpjc.setConcurrency(concurrency);

			NamedThreadPoolExecutor writerPool = new NamedThreadPoolExecutor(
					dpjc.getName() + "-" + dpjc.getId(), dpjc.getConcurrency(),
					dpjc.getConcurrency(), 1L, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>());

			writerPool.setPostWorker(writerWorkerForPreAndPost);
			writerPool.setParam(writerParam);

			writerPool.prestartAllCoreThreads();
			writerPoolList.add(writerPool);
			logger.info("DataX Writer starts to write data .");

			for (PluginParam pp : writerSplitParams) {
				WriterWorker writerWorker = new WriterWorker(writerConf,
						myClass);
				writerWorker.setParam(pp);
				writerWorker.setLineReceiver(new BufferedLineExchanger(sp
						.getStorageForWriter(dpjc.getId()), null,
						this.engineConf.getStorageBufferSize(),
						this.writerLineFactory));
				writerPool.execute(writerWorker);
				writerMonitorPool.monitor(writerWorker);
			}
		}
		return writerPoolList;
	}
	
	/**
	 * @return the storagePool
	 */
	public List<Statistics> getStatistics() {
		List<Statistics> statistics = new ArrayList<Storage.Statistics>();
		for (Storage storage : storagePool.getStorageForReader()) {
			statistics.add(storage.getStat());
		}
		return statistics;
	}

}
