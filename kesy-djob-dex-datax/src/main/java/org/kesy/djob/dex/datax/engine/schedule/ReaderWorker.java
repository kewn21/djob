
package org.kesy.djob.dex.datax.engine.schedule;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.kesy.djob.dex.datax.common.exception.DataExchangeException;
import org.kesy.djob.dex.datax.common.exception.ExceptionTracker;
import org.kesy.djob.dex.datax.common.plugin.LineSender;
import org.kesy.djob.dex.datax.common.plugin.Pluginable;
import org.kesy.djob.dex.datax.common.plugin.Reader;
import org.kesy.djob.dex.datax.engine.conf.PluginConf;

/**
 * {@link ReaderWorker} represents executor of {@link Reader} </br>
 * {@link Engine} use {@link ReaderWorker} to read data.</br>
 * 
 * */
public class ReaderWorker extends PluginWorker implements Runnable {
	private LineSender sender;

	private Method connect;

	private Method startRead;

	private Method finish;

	private static int globalIndex = 0;

	private static final Log logger = LogFactory.getLog(ReaderWorker.class);
	
	/**
	 * Constructor for {@link ReaderWorker}.
	 * 
	 * @param	pluginConf
	 * 			{@link PluginConf}
	 * 
	 * @param	myClass
	 * 			class of {@link Pluginable}
	 * 
	 * @throws		{@link DataExchangeException}
	 * 
	 * */
	public ReaderWorker(PluginConf pluginConf, Class<?> myClass) {
		super(pluginConf, myClass);
		try {
			connect = myClass.getMethod("connect", new Class[] {});
			startRead = myClass
					.getMethod("startRead", new Class[] { Class
							.forName("org.kesy.djob.dex.datax.common.plugin.LineSender") });
			finish = myClass.getMethod("finish", new Class[] {});
		}  catch (Exception e) {
			logger.error(ExceptionTracker.trace(e));
			throw new DataExchangeException(e);
		}
		this.setMyIndex(globalIndex++);
	}


	/**
	 * Read data, main execute logic code of {@link Reader} <br>
	 * NOTE: When catches exception, {@link ReaderWorker} exit process immediately.
	 * 
	 * */
	@Override
	public void run() {
		try {
			int iRetcode = (Integer) init.invoke(myObject, new Object[] {});
			if (iRetcode != 0) {
				logger.error("Reader initialize failed .");
				//System.exit(ExitStatus.FAILED.value());
				return;
			}
			iRetcode = (Integer) connect.invoke(myObject,
					new Object[] {});
			if (iRetcode != 0) {
				logger.error("Reader connect to DB failed .");
				//System.exit(ExitStatus.FAILED.value());
				return;
			}
			iRetcode = (Integer) startRead.invoke(myObject,
					new Object[] { this.sender });
			if (iRetcode != 0) {
				logger.error("Reader startRead failed .");
				//System.exit(ExitStatus.FAILED.value());
				return;
			}
			iRetcode = (Integer) finish.invoke(myObject, new Object[] {});
			if (iRetcode != 0) {
				logger.error("Reader finish loading failed .");
				//System.exit(ExitStatus.FAILED.value());
				return;
			}
		}  catch (Exception e) {
			logger.error(ExceptionTracker.trace(e));
			//System.exit(ExitStatus.FAILED.value());
		}
	}
	
	public void setLineSender(LineSender sender) {
		this.sender = sender;
	}

}
