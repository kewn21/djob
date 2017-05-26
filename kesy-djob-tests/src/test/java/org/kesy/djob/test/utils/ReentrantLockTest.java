/**
 * 2014年12月30日
 */
package org.kesy.djob.test.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kewn
 *
 */
public class ReentrantLockTest {
	
	public static enum JobDepentStatus {
		waiting,
		running,
		finish
	}
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ReentrantLock lock = new ReentrantLock();
	private Condition await = lock.newCondition();
	
	private Condition doSomething = lock.newCondition();
	
	private Timer timer = new Timer(true);
	//private static final long DEFAULT_INTERVAL = 1 * 60 * 1000;
	private static final long DEFAULT_INTERVAL = 5 * 60 * 1000;
	
	@Test
	public void test01() throws InterruptedException {
		getDepentStatus("201412");
	}
	
	public JobDepentStatus getDepentStatus(final String date) throws InterruptedException {
		
		final String jobName = "test";
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				logger.info("Drainer current dependences status for job [{}] on [{}]", jobName, date);

				boolean lck = lock.tryLock();
				logger.info("Get lock successful : [{}]", lck);
				
				/*try {
					lock.lockInterruptibly();
				} catch (InterruptedException e1) {
				}*/
				
				lock.lock();
				
				
				logger.info("Current thread : [{}]", Thread.currentThread().getName());
				
				try {
					logger.info("sleeping 5000 ms...");
					Thread.sleep(5000);
					JobDepentStatus status = JobDepentStatus.finish;
					logger.info("Current dependences status of job [{}] on [{}] -> [{}]", jobName, date, status);
					
					//doSomething.await();
					
					if (JobDepentStatus.finish == status) {
						await.signal();
					}
				} catch (Exception e) {
					logger.error("signal error", e);
				} finally {
					logger.info("release lock...");
					lock.unlock();
					//lock.unlock();
				}
			}
		}, 0, DEFAULT_INTERVAL);
		
		new Thread(new TimerTask() {
			
			@Override
			public void run() {
				logger.info("t Drainer current dependences status for job [{}] on [{}]", jobName, date);
				
				boolean lck = lock.tryLock();
				logger.info("t Get lock successful : [{}]", lck);
				
				/*try {
					lock.lockInterruptibly();
				} catch (InterruptedException e1) {
				}*/
				
				lock.lock();
				
				logger.info("t Current thread : [{}]", Thread.currentThread().getName());
				
				try {
					logger.info("t sleeping 5000 ms...");
					Thread.sleep(5000);
					JobDepentStatus status = JobDepentStatus.finish;
					logger.info("t Current dependences status of job [{}] on [{}] -> [{}]", jobName, date, status);
					
					if (JobDepentStatus.finish == status) {
						await.signal();
					}
				} catch (Exception e) {
					logger.error("t signal error", e);
				} finally {
					logger.info("t release lock...");
					lock.unlock();
					//lock.unlock();
				}
			}
		}).start();

		//lock.lock();
		boolean lck = lock.tryLock();
		logger.info("m Get lock successful : [{}]", lck);
		
		lck = lock.tryLock();
		logger.info("m Get lock successful : [{}]", lck);
		
		try {
			logger.info("m Current thread : [{}]", Thread.currentThread().getName());
			
			logger.info("m Waiting for dependences status of job [{}] on [{}].", jobName, date);
			
			logger.info("Sleep to see whether current thread dosen't hold the lock.");
			Thread.sleep(5000);
			logger.info("Already finished sleep.");
			
			await.await();
			logger.info("m All dependences status of job [{}] on [{}] are finished.", jobName, date);
			
			//doSomething.notify();
			
			logger.info("notify doSomething...");
		} catch (InterruptedException e) {
			logger.error("m Waiting for dependences status of job [{}] on [{}]", jobName, date, e);
		} finally {
			lock.unlock();
			lock.unlock();
		}
		
		timer.cancel();
		
		//Thread.sleep(20000);

		return JobDepentStatus.finish;
	}
}
