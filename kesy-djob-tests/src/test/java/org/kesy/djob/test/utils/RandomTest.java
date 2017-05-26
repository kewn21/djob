/**
 * 2014年9月18日
 */
package org.kesy.djob.test.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kewn
 *
 */
public class RandomTest {
	
	private final static String FILE_CHARSET = "UTF-8";
	private final static String FILE_NAME = "random_string_20150722.txt";
	@SuppressWarnings("deprecation")
	private final static String FILE_PATH = URLDecoder.decode(
			ClassLoader.class.getResource("/").getPath() + FILE_NAME);
	
	//生成的随机码位数
	private final static int BIT_NUM = 12;
	//生成的随机码数量
	private final static long TOTAL_NUM = 200000;
	//生成的随机码前缀
	private final static String PREX = "LYQB";
	
	/**
	 * 每位允许的字符
	 */
	private static final String POSSIBLE_CHARS="1234567890ABCDEF";
	
	/**
	 * 生产一个指定长度的随机字符串
	 * @param length 字符串长度
	 * @return
	 */
	private String generateRandomString(int length) {
		StringBuilder sb = new StringBuilder(length);
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS.length())));
		}
		return sb.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedWriter writer = null;
		
		System.out.println("---------------------start to write data---------------------");
		
		long startTime = System.currentTimeMillis();
		System.out.println("start time : " + startTime);
		
		try {
			System.out.println("write to " + FILE_PATH);
			
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(FILE_PATH)), FILE_CHARSET));
			
			Set<String> check = new HashSet<String>();
			RandomTest obj = new RandomTest();
			
			for (int i = 1; i <= TOTAL_NUM; i++) {
				String s = obj.generateRandomString(BIT_NUM);
				if (check.contains(s)) {
					throw new IllegalStateException("Repeated string found : " + s);
				} else {
					if (i % 1000 == 0) 
						System.out.println("generated " + i / 1000 + "000 strings.");
					
					check.add(s);
					writer.write(PREX + s + "\n");
				}
			}
			
			writer.flush();
			
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				System.err.println(e);
			} finally {
				long finishTime = System.currentTimeMillis();
				
				System.out.println("finish time : " + startTime);
				System.out.println("writing cost time : " + (finishTime - startTime));
				System.out.println("---------------------finish to write data---------------------");
			}
		}
	}

}
