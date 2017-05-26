
package org.kesy.djob.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;


/**
 * TODO : document me
 * 
 * @author kewn
 */
public class SpringFactory implements BeanFactoryAware {
	
	public static void load() {
		new BeanFactoryImpl();
	}

	private static BeanFactory FACTORY = null;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		FACTORY = beanFactory;
	}
	
	
	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (!FACTORY.containsBean(beanName)) {
			return null;
		}
		return FACTORY.getBean(beanName, clazz);
	}
	
	public static Object getBean(String beanName) {
		return FACTORY.getBean(beanName);
	}
	
	public static <T> T getBean(Class<T> requiredType) {
		return FACTORY.getBean(requiredType);
	}

}
