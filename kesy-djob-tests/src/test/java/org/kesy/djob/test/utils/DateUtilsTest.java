/**
 * 2014年9月4日
 */
package org.kesy.djob.test.utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.kesy.djob.core.utils.DateUtils;

/**
 * @author kewn
 *
 */
public class DateUtilsTest {
	
	@Test
	public void test01() throws ParseException {
		System.out.println(
				DateUtils.toDate(20140904));
	}
	
	@Test
	public void test02() throws ParseException {
		List<Date> dates =  DateUtils.getBetweenDates(20140904, 20140905, 1);
		for (int i = 0; i < dates.size(); i++) {
			System.out.println(dates.get(i));
		}
	}
	
	@Test
	public void test03() throws ParseException {
		List<Date> dates =  DateUtils.getBetweenDates(20140904, 20140904, 1);
		for (int i = 0; i < dates.size(); i++) {
			System.out.println(dates.get(i));
		}
	}
	
	@Test
	public void test04() throws ParseException {
		List<Date> dates =  DateUtils.getBetweenDates(20140831, 20140901, 1);
		for (int i = 0; i < dates.size(); i++) {
			System.out.println(dates.get(i));
		}
	}
	
	@Test
	public void test05() throws ParseException {
		List<Date> dates =  DateUtils.getBetweenDates(20140831, 20140830, 1);
		for (int i = 0; i < dates.size(); i++) {
			System.out.println(dates.get(i));
		}
	}

	@Test
	public void test06() throws ParseException {
		List<Date> dates =  DateUtils.getBetweenDates(20140906, 20140910, 1);
		for (int i = 0; i < dates.size(); i++) {
			System.out.println(dates.get(i));
		}
	}
	
	@Test
	public void test07() throws ParseException {
		System.out.println(DateUtils.today());
	}
	
	@Test
	public void test08() throws ParseException, UnsupportedEncodingException {
		String u8 = "abc";
		String g = "abc";
		
		byte[] b8 = u8.getBytes("utf-8");
		
		System.out.println(new String(b8, "gb2312"));
	}
	
	@Test
	public void test081() throws ParseException, UnsupportedEncodingException {
		String u8 = "我们";
		String g = "abc";
		
		byte[] b8 = u8.getBytes("utf-8");
		
		System.out.println(new String(b8, "gb2312"));
	}
	
	
	@Test
	public void test09() throws ParseException, UnsupportedEncodingException {
		String u8 = "abc";
		String g = "abc";
		
		byte[] b8 = u8.getBytes("gb2312");
		
		System.out.println(new String(b8, "utf-8"));
	}
	
	
	@Test
	public void test091() throws ParseException, UnsupportedEncodingException {
		String u8 = "我们";
		String g = "abc";
		
		byte[] b8 = u8.getBytes("gb2312");
		
		System.out.println(new String(b8, "utf-8"));
	}
	
	@Test
	public void test10() throws ParseException, UnsupportedEncodingException {
		String u8 = "abc";
		String g = "abc";
		
		byte[] b8 = u8.getBytes("utf-8");
		byte[] bg = g.getBytes("gb2312");
		
		System.out.println(b8);
		System.out.println(bg);
		
		System.out.println(new String(b8, "gb2312"));
		System.out.println(new String(bg, "utf-8"));
	}
	
	@Test
	public void test11() throws ParseException, UnsupportedEncodingException {
		String u8 = "abc";
		String g = "abc";
		
		byte[] b8 = u8.getBytes("utf-8");
		byte[] bg = g.getBytes("gbk");
		
		System.out.println(b8);
		System.out.println(bg);
		
		System.out.println(new String(b8, "gbk"));
		System.out.println(new String(bg, "utf-8"));
	}
}
