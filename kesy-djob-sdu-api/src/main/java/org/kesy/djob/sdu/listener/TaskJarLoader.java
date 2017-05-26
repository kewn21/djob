/**
 * This file created at Feb 7, 2014.
 *
 */
package org.kesy.djob.sdu.listener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * 类<code>{@link TaskJarLoader}</code>  创建于 Feb 7, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class TaskJarLoader extends URLClassLoader {

	/**
	 * @param urls
	 * @param parent
	 * @param factory
	 */
	public TaskJarLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	/**
	 * @param urls
	 * @param parent
	 */
	public TaskJarLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	/**
	 * @param urls
	 */
	public TaskJarLoader(URL[] urls) {
		super(urls);
	}

	@Override
	//2014-02-10 simple version 
	protected Class<?> findClass(String className) throws ClassNotFoundException {
		try {
			Class<?> clazz = super.findClass(className);
			if (null != clazz) {
				return clazz;
			}
		} catch (ClassNotFoundException e) {
			//ignore
		}

		///1、构造class File
		int dotIndex = className.indexOf(".");
		String separator = File.separator;
		if (separator.equals("\\")) {
			separator = "\\\\";
		}
		String classFile;
		if (dotIndex != -1) {
			classFile = new StringBuilder().append(className.replaceAll("\\.", separator)).append(".class").toString();
		} else {
			classFile = new StringBuilder().append(className).append(".class").toString();
		}
		
		//2、遍历classpath
		URL[] urls = super.getURLs();// 注意这个版本的支持的协议都是 file local，而且这里每次动态的遍历会非常费时
		// 时间= job个数* listener个数*一次遍历时间
		List<File> entries = new ArrayList<File>();
		if (null == urls) {
			throw new ClassNotFoundException(className);
		}

		for (URL url : urls) {
			File f1 = new File(url.getFile());
			if (f1.isDirectory()) {
				entries.addAll(Arrays.asList(f1.listFiles()));
			} else {
				entries.add(f1);
			}
		}
		
		//3、匹配classFile、打开class输入流并定义class
		URL res = getLocalResource(classFile, entries);
		if (res == null) {
			throw new ClassNotFoundException(className);
		}
		InputStream ins = null;
		try {
			ins = res.openStream();
			BufferedInputStream bis = new BufferedInputStream(ins);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024 * 10];
			int readBytes;
			while ((readBytes = bis.read(bytes)) != -1) {
				os.write(bytes, 0, readBytes);
			}
			byte[] b = os.toByteArray();
			return defineClass(className, b, 0, b.length);
		} catch (IOException e) {
			throw new ClassNotFoundException(e.getMessage(), e);
		}

	}

	private URL getLocalResource(String name, List<File> entries) {
		for (int i = 0; i < entries.size(); i++) {
			File entry = entries.get(i);
			if (entry.isDirectory() && entry.exists()) {
				File f = new File(entry, name);
				if (f.exists()) {
					URL url;
					try {
						url = f.toURI().toURL();

					} catch (MalformedURLException ex) {
						continue;
					}
					return url;
				}
			} else if (entry.isFile() && entry.exists()) {
				URL url = null;
				ZipFile zf = null;
				try {
					zf = new ZipFile(entry);
					name = name.replaceAll("\\\\", "/");
					ZipEntry zipEntry = zf.getEntry(name);
					if (zipEntry == null) {
						continue;
					}
					String url_0_ = entry.getAbsolutePath().replaceAll("\\\\", "/");
					if (!url_0_.startsWith("/")) {
						url_0_ = new StringBuilder().append("/").append(url_0_).toString();
					}
					url = new URL(new StringBuilder().append("jar:file://").append(url_0_).append("!/").append(name)
							.toString());
				} catch (ZipException zipexception) {
					zipexception.printStackTrace();
				} catch (IOException ioexception) {
					ioexception.printStackTrace();
				} finally {
					try {
						if (zf != null) {
							zf.close();
						}
					} catch (Exception e) {
					}
				}
				return url;
			}
		}
		return null;
	}

}
