/**
 * This file created at Jan 13, 2014.
 *
 */
package org.kesy.djob.sdu.listener;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kesy.djob.core.json.Xml;
import org.kesy.djob.core.spring.SpringFactory;
import org.kesy.djob.sdu.api.JobListener;
import org.kesy.djob.sdu.api.task.TaskExecutor;
import org.kesy.djob.sdu.listener.config.Listener;
import org.kesy.djob.sdu.listener.config.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 类<code>{@link ListenerRegistry}</code>  创建于 Jan 13, 2014<br/>
 * 类描述:job的通知者切面注册器<p>
 * <li></li>
 * @author kewn
 */
public final class ListenerRegistry {

	private static final Logger logger = LoggerFactory.getLogger(ListenerRegistry.class);
	
	private static final String						TASK_LOCATION	= "classpath*:/job-plugins/**/task-plugin*.xml";
	private static final String						FILE_LINTENER	= "/job-plugins/listener-plugin.xml";
	
    private static Map<String, List<JobListener>> listenerMap;
    
    private static List<JobListener> jobListeners;
    private static List<Listener> listeners;
    
    private static Object lckObj = new Object();
    
	/**
	 * key:jobId,value:类全名
	 */
	private static Map<String, String> taskConfigMap;

	
	static {
		//首先从xml初始化
		try {
			listenerMap = new HashMap<String, List<JobListener>>();
			taskConfigMap = new HashMap<String, String>();
			
			List<Task> tasks = new ArrayList<Task>();
			ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resourcePatternResolver.getResources(TASK_LOCATION);
			for (Resource resource : resources) {
				URL url = resource.getURL();
				try {
					tasks.addAll(Xml.toList(Task.class, url.getPath()));
				} catch (Exception e) {
					logger.error("load [{}] error \n\t", url.getPath(), e);
				}
			}
			
			listeners = Xml.toList4jar(Listener.class, FILE_LINTENER);
			
			Map<String, JobListener> jobListenerMap = new HashMap<String, JobListener>();
			
			for (Task task : tasks) {
				String[] listenerArray = task.getListeners().split(",");
				List<JobListener> jobListeners = new ArrayList<JobListener>();
				
				for (String listenerStr : listenerArray) {
					for (Listener listener : listeners) {
						String listenerName = listener.getName();
						if (listenerName.equals(listenerStr)) {
							if (jobListenerMap.containsKey(listenerStr)) {
								jobListeners.add(jobListenerMap.get(listenerStr));
							}
							else {
								String listenerClass = listener.getClazz();
								Class<?> clazz = Class.forName(listenerClass);
								JobListener jobListener = (JobListener)clazz.newInstance();
								jobListener.init(listener.getParams());
								jobListeners.add(jobListener);
								
								jobListenerMap.put(listenerStr, jobListener);
							}
							break;
						}
					}
				}
				
				taskConfigMap.put(task.getName(), task.getClazz());
				listenerMap.put(task.getName(), jobListeners);
			}
			
			logger.info("task count : " + tasks.size());
			logger.info("listener count : " + listeners.size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			taskConfigMap.clear();
			listenerMap.clear();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 功能描述:获取一个job的所有通知对象
	 * @param execName
	 * @return
	 * @author kewn
	 */
	public static List<JobListener> getListeners(String execName) {
		if (execName.startsWith("spring:")) {
			if (jobListeners != null) {
				return jobListeners;
			}
			
			synchronized (lckObj) {
				if (jobListeners != null) {
					return jobListeners;
				}
				
				try {
					List<JobListener> tmpJobListeners = new ArrayList<JobListener>();
					for (Listener listener : listeners) {
						String listenerClass = listener.getClazz();
						Class<?> clazz = Class.forName(listenerClass);
						JobListener jobListener = (JobListener)clazz.newInstance();
						jobListener.init(listener.getParams());
						tmpJobListeners.add(jobListener);
					}
					 
					jobListeners = tmpJobListeners;
					tmpJobListeners = null;
					return jobListeners;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
		
		return listenerMap.get(execName);
	}

	/**
	 * 功能描述:获取job的{@link TaskExecutor}类全名
	 * @param execName
	 * @return
	 * @author kewn
	 */
	public static String getTaskClassName(String execName) {
		return taskConfigMap.get(execName);
	}
	
	public static TaskExecutor getTaskInstance(String execName) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (execName.startsWith("spring:")) {
			String beanName = execName.split(":")[1];
			return SpringFactory.getBean(beanName, TaskExecutor.class);
		}
		
		String className = getTaskClassName(execName);
		if (StringUtils.isNotEmpty(className)) {
			Class<?> clazz = Class.forName(className);
			if (clazz != null) {
				return (TaskExecutor)clazz.newInstance();
			}
		}
		
		logger.error("instance task " + execName + " error");
		return null;
	}

	/**
	 * 功能描述:增加一个job的通知处理对象
	 * @param execName
	 * @param jobListener
	 * @author kewn
	 */
	public static void addListener(String execName, JobListener jobListener) {
		if (listenerMap.keySet().contains(execName)) {
			//不检查对象是否已经存在
			listenerMap.get(execName).add(jobListener);
		} else {
			List<JobListener> listeners = new ArrayList<JobListener>();
			listeners.add(jobListener);
			listenerMap.put(execName, listeners);
		}
	}

	public static void reload() {

	}

	public static void removeListener() {

	}
}
