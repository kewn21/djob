
package org.kesy.djob.dex.datax.engine.schedule;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.kesy.djob.dex.datax.common.plugin.PluginParam;

/**
 * {@link NamedThreadPoolExecutor} is a simple wrapper for ThreadPoolExecutor.
 * 
 * */
public class NamedThreadPoolExecutor extends ThreadPoolExecutor {
	String name;

	PluginParam param;

	PluginWorker postWorker;

	/**
	 * Constructor of {@link NamedThreadPoolExecutor}
	 * 
	 * @param	name
	 * 			name of the threadpool.
	 * 
	 * @param	corePoolSize
	 * 			number of pool core threads.
	 * 
	 * @param	maximumPoolSize
	 * 			number of pool max threads.
	 * 
	 * @param	keepAliveTime
	 *  		when the number of threads is greater than </br>
     * 			the core, this is the maximum time that excess idle threads </br>
     * 			will wait for new tasks before terminating. </br>
     * 
     * @param	unit
     * 			unit the time unit for the keepAliveTime argument.
     * 
     * @param	workQueue
     * 			workQueue the queue to use for holding tasks before they </br>
     *			are executed. This queue will hold only the <tt>Runnable</tt> </br>
     * 			tasks submitted by the <tt>execute</tt> method. <br>
	 * */
	public NamedThreadPoolExecutor(String name, int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void doPrepare() {
		postWorker.prepare(param);
	}

	public void doPost() {
		postWorker.post(param);
	}

	public PluginParam getParam() {
		return param;
	}

	public void setParam(PluginParam param) {
		this.param = param;
	}

	public PluginWorker getPostWorker() {
		return this.postWorker;
	}

	public void setPostWorker(PluginWorker workerInPool) {
		this.postWorker = workerInPool;
	}

}
