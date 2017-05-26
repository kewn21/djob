/**
 * 2014年9月18日
 */
package org.kesy.djob.test.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author kewn
 *
 */
public class ResTest {

	
	@Test
	public void test01() {
		Resource configResource = new ClassPathResource("classpath*:spring/*.xml");
		System.out.println(configResource);
	}
	
	@Test
	public void test02() throws IOException {
		URL url = ClassLoader.getSystemResource("");
		File file = new File(url.getPath() + "job-plugins/");
		String[] files = file.list();
		for (String f : files) {
			if (f.startsWith("task-plugin")) {
				System.out.println(f);
			}
		}
		
		/*System.out.println(url);
		Enumeration<URL> urls = ClassLoader.getSystemResources("/job-plugins/");
		while ((url = urls.nextElement()) != null) {
			System.out.println(url);
		}*/
	}
}
