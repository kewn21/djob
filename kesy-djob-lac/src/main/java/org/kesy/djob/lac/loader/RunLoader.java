package org.kesy.djob.lac.loader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


public class RunLoader {
	
	public static void main(String[] args) {
        start(args);
	}
	

	private static List<URL> getUrls(String[] paths) throws MalformedURLException {
		if (null == paths || 0 == paths.length) {
			throw new IllegalArgumentException("Paths cannot be empty .");
		}

		List<URL> urls = new ArrayList<URL>();
		for (String path : paths) {
			getUrl(path, urls);
		}
		
		return urls;
	}

	private static void getUrl(String path, List<URL> urls) throws MalformedURLException {
		File jarPath = new File(path);
		recursionFile(jarPath, urls);
	}
	
	private static void recursionFile(File file, List<URL> jarUrls) throws MalformedURLException {
		if (file.isDirectory()) {
			
			jarUrls.add(file.toURI().toURL());
			
			File[] allJars = file.listFiles();
			for (File childFile : allJars) {
				
				if (!childFile.isDirectory() && 
					childFile.getName().endsWith(".jar")) {
					
					jarUrls.add(childFile.toURI().toURL());
				}
				else {
					recursionFile(childFile, jarUrls);
				}
			}
		}
	}
	
	public static void start(String[] args) {
		try {
			String currPath = System.getProperty("user.dir");
			int lastIndex = currPath.lastIndexOf(File.separator);
			String procPath = currPath.substring(0, lastIndex);
			
			List<URL> urls = getUrls(new String[] 
			{ 
					procPath + "/lib",
					procPath + "/plugins",
					procPath + "/dropins"
			});
			
			urls.add(new File(procPath + "/conf").toURI().toURL());
			
			URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader(); 
	        Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class }); 
	        add.setAccessible(true); 
			
			for (URL url : urls) {
				add.invoke(classloader, new Object[] { url }); 
			}
			
			Class clazz = ClassLoader.getSystemClassLoader().loadClass("org.kesy.djob.lac.Startup");
			Method method = clazz.getMethod("start");
			Object object = clazz.newInstance();
			method.invoke(object);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public static void stop(String[] args) {
		
		try {
			Class clazz = ClassLoader.getSystemClassLoader().loadClass("org.kesy.djob.lac.Shutdown");
			Method method = clazz.getMethod("stop");
			Object object = clazz.newInstance();
			method.invoke(object);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}

}
