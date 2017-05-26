/**
 * 2014年8月26日
 */
package org.kesy.djob.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author kewn
 *
 */
public class BeanFactoryImpl implements BeanFactory {

	private ApplicationContext context = new ClassPathXmlApplicationContext(
			"classpath*:spring/*.xml");

	@Override
	public boolean containsBean(String arg0) {
		return context.containsBean(arg0);
	}

	@Override
	public String[] getAliases(String arg0) {
		// TODO implement BeanFactory.getAliases
		return null;
	}

	@Override
	public Object getBean(String arg0) throws BeansException {
		return context.getBean(arg0);
	}

	@Override
	public <T> T getBean(Class<T> arg0) throws BeansException {
		return context.getBean(arg0);
	}

	@Override
	public <T> T getBean(String arg0, Class<T> arg1) throws BeansException {
		return context.getBean(arg0, arg1);
	}

	@Override
	public Object getBean(String arg0, Object... arg1)
			throws BeansException {
		// TODO implement BeanFactory.getBean
		return null;
	}

	@Override
	public Class<?> getType(String arg0)
			throws NoSuchBeanDefinitionException {
		// TODO implement BeanFactory.getType
		return null;
	}

	@Override
	public boolean isPrototype(String arg0)
			throws NoSuchBeanDefinitionException {
		// TODO implement BeanFactory.isPrototype
		return false;
	}

	@Override
	public boolean isSingleton(String arg0)
			throws NoSuchBeanDefinitionException {
		// TODO implement BeanFactory.isSingleton
		return false;
	}

	@Override
	public boolean isTypeMatch(String arg0, Class<?> arg1)
			throws NoSuchBeanDefinitionException {
		// TODO implement BeanFactory.isTypeMatch
		return false;
	}
}
