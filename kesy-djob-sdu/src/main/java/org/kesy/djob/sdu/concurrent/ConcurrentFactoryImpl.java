/**
 * 2014年8月26日
 */
package org.kesy.djob.sdu.concurrent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.kesy.djob.sdu.api.JobSchedulerConf;

/**
 * @author kewn
 *
 */
public class ConcurrentFactoryImpl extends ConcurrentFactory {
	
	private static Queue<TaskRunner> waitingQueue = new ConcurrentLinkedQueue<TaskRunner>();
	private static List<TaskRunner> runningList = new ArrayList<TaskRunner>();
	
	private static volatile int concurrentCnt;
	private static int MAX_CONCURRENT_COUNT = JobSchedulerConf.ACTIVE_JOB_COUNT;
	
	private Object lockObj = new Object();
	

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.concurrent.ConcurrentFactory#execute(org.kesy.djob.sdu.concurrent.Runner)
	 */
	@Override
	public boolean execute(TaskRunner runner) throws Exception {
		synchronized (lockObj) {
			if (concurrentCnt >= MAX_CONCURRENT_COUNT) {
				waitingQueue.add(runner);
				return false;
			}
			
			concurrentCnt++;
			runningList.add(runner);
		}
		
		if (runner != null) {
			runner.start();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.concurrent.ConcurrentFactory#stop(java.lang.String)
	 */
	@Override
	public void stop(String id) throws Exception {
		for (int i = 0; i < runningList.size(); i++) {
			if (runningList.get(i).getTaskId().equals(id)) {
				concurrentCnt--;
				runningList.remove(runningList.get(i));
				runningList.get(i).stop();
				return;
			}
		}
		
		synchronized (lockObj) {
			TaskRunner matchedRunner = null;
			Iterator<TaskRunner> iterable = waitingQueue.iterator();
			while (iterable.hasNext()) {
				TaskRunner runner = (TaskRunner) iterable.next();
				if (runner.getTaskId().equals(id)) {
					matchedRunner = runner;
					break;
				}
			}
			
			if (matchedRunner != null) {
				waitingQueue.remove(matchedRunner);
				matchedRunner = null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.sdu.concurrent.ConcurrentFactory#next(java.lang.String)
	 */
	@Override
	public void next(String id) throws Exception {
		//clear
		for (int i = 0; i < runningList.size(); i++) {
			if (runningList.get(i).getTaskId().equals(id)) {
				concurrentCnt--;
				runningList.remove(runningList.get(i));
				break;
			}
		}
		
		TaskRunner runner = null;
		synchronized (lockObj) {
			//executting the next
			runner = waitingQueue.poll();
			if (runner == null) {
				return;
			}
			
			if (concurrentCnt >= MAX_CONCURRENT_COUNT) {
				waitingQueue.add(runner);
				return;
			}
			
			concurrentCnt++;
			runningList.add(runner);
		}
		
		if (runner != null) {
			runner.start();
		}
	}
}
