/**
 * 2014年9月4日
 */
package org.kesy.djob.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author kewn
 *
 */
public final class DateUtils {
	
	public static Date toDate(long longDay) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.parse(String.valueOf(longDay));
	}
	
	public static List<Date> getBetweenDates(long startLongDay, long endLongDay, int interval) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		
		Calendar calendar = Calendar.getInstance(); 
		
		Date endDate = formatter.parse(String.valueOf(endLongDay));
		calendar.setTime(endDate);  
        long endTime = calendar.getTimeInMillis();  
        
        Date startDate = formatter.parse(String.valueOf(startLongDay));
        calendar.setTime(startDate);  
        long startTime = calendar.getTimeInMillis();  
        
        List<Date> dates = new ArrayList<Date>();
        dates.add(startDate);
        
        long betweenDays = (endTime - startTime) / (1000 * 3600 * 24 * interval);  
        if (betweenDays == 0) {
			return dates;
		}
        
        for (int i = 1; i < betweenDays; i++) {
        	calendar.add(Calendar.DATE, interval);
        	dates.add(calendar.getTime());
		}
        
        dates.add(endDate);
        
        return dates;
	}
	
	public static List<String> getBetweenDateStrings(long startLongDay, long endLongDay, int interval) throws ParseException {
		return getBetweenDateStrings(startLongDay, endLongDay, interval, "yyyyMMdd", "yyyyMMdd");
	}
	
	public static List<String> getBetweenDateStrings(long startLongDay, long endLongDay, int interval, String sformat, String tformat) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		
		Calendar calendar = Calendar.getInstance(); 
		
		Date endDate = formatter.parse(String.valueOf(endLongDay));
		calendar.setTime(endDate);  
        long endTime = calendar.getTimeInMillis();  
        
        Date startDate = formatter.parse(String.valueOf(startLongDay));
        calendar.setTime(startDate);  
        long startTime = calendar.getTimeInMillis();  
        
        
        formatter = new SimpleDateFormat(tformat);
        
        List<String> dates = new ArrayList<String>();
        dates.add(formatter.format(startDate));
        
        long betweenDays = (endTime - startTime) / (1000 * 3600 * 24 * interval);  
        if (betweenDays == 0) {
			return dates;
		}
        
        for (int i = 1; i < betweenDays; i++) {
        	calendar.add(Calendar.DATE, interval);
        	dates.add(formatter.format(calendar.getTime()));
		}
        
        dates.add(formatter.format(endDate));
        
        return dates;
	}
	
	public static List<String> getBetweenMonthStrings(long startLongMonth, long endLongMonth, int interval) throws ParseException {
		return getBetweenMonthStrings(startLongMonth, endLongMonth, interval, "yyyyMM", "yyyyMM");
	}
	
	public static List<String> getBetweenMonthStrings(long startLongMonth, long endLongMonth, int interval, String sformat, String tformat) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		
		Calendar calendar = Calendar.getInstance(); 
		
		Date endDate = formatter.parse(String.valueOf(endLongMonth));
		calendar.setTime(endDate);  
        
        Date startDate = formatter.parse(String.valueOf(startLongMonth));
        calendar.setTime(startDate);  
        
        formatter = new SimpleDateFormat(tformat);
        
        List<String> dates = new ArrayList<String>();
        dates.add(formatter.format(startDate));

        calendar.setTime(startDate);  
        calendar.add(Calendar.MONTH, interval);
        startDate = calendar.getTime();
        
        while (startDate.equals(endDate) || startDate.before(endDate)) {
            dates.add(formatter.format(startDate));
            
            calendar.add(Calendar.MONTH, interval);
            startDate = calendar.getTime();
		}
        
        return dates;
	}
	
	public static long today() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return Long.valueOf(formatter.format(new Date()));
	}
	
	public static String getNowDateString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");  
		return formatter.format(new Date());
	}
	
	public static String getNowMinuteString() {
		return getNowMinuteString("yyyyMMddHHmm");
	}
	
	public static String getNowMinuteString(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);  
		return formatter.format(new Date());
	}
	
	public static String getYesterdayString() {
		return getYesterdayString("yyyyMMdd");
	}
	
	public static String getYesterdayString(String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		Date date = calendar.getTime();
		
		SimpleDateFormat formatter = new SimpleDateFormat(format);  
		return formatter.format(date);
	}
	
	public static String toYesterdayString(String dateStr) {
		return toYesterdayString(dateStr, "yyyyMMdd", "yyyyMMdd");
	}
	
	public static String toYesterdayString(String dateStr, String sformat, String tformat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(sformat);  
			Date date = formatter.parse(dateStr);
					
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -1);
			date = calendar.getTime();
			
			formatter = new SimpleDateFormat(tformat);  
			return formatter.format(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String toLastMonthString(String dateStr) {
		return toLastMonthString(dateStr, "yyyyMM", "yyyyMM");
	}
	
	public static String toLastMonthString(String dateStr, String sformat, String tformat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(sformat);  
			Date date = formatter.parse(dateStr);
					
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, -1);
			date = calendar.getTime();
			
			formatter = new SimpleDateFormat(tformat);  
			return formatter.format(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getLastMonthString(String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		Date date = calendar.getTime();
		
		SimpleDateFormat formatter = new SimpleDateFormat(format);  
		return formatter.format(date);
	}
	
	public static String plusMinuteString(String dateStr, int plusMinutes) {
		return plusMinuteString(dateStr, plusMinutes, "yyyyMMddHHmm");
	}
	
	public static String plusMinuteString(String dateStr, int plusMinutes, String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date date = formatter.parse(dateStr);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MINUTE, plusMinutes);
			String str = formatter.format(c.getTime());
			return str;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static List<Date> getBetweenMinutes(long startLongMinute, long endLongMinute, int interval) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		
		Calendar calendar = Calendar.getInstance(); 
		
		int startMinute = (int)(startLongMinute - startLongMinute / 10 * 10);
		int endMinute = (int)(endLongMinute - endLongMinute / 10 * 10);
		
		if (startMinute > 0 && startMinute < 5) {
			startMinute = 5;
		}
		else if (startMinute > 5) {
			startMinute = 10;
		}
		
		if (endMinute > 0 && endMinute < 5) {
			endMinute = 0;
		}
		else if (endMinute > 5) {
			endMinute = 5;
		}
		
		startLongMinute = startLongMinute / 10 * 10;
		endLongMinute = endLongMinute / 10 * 10;
		
		Date endDate = formatter.parse(String.valueOf(endLongMinute));
		calendar.setTime(endDate);  
		calendar.add(Calendar.MINUTE, endMinute);
		endDate = calendar.getTime();
        long endTime = calendar.getTimeInMillis(); 
        
        Date startDate = formatter.parse(String.valueOf(startLongMinute));
        calendar.setTime(startDate);  
        calendar.add(Calendar.MINUTE, startMinute);
        startDate = calendar.getTime();
        long startTime = calendar.getTimeInMillis();  
        
        List<Date> dates = new ArrayList<Date>();
        dates.add(startDate);
        
        long betweenMinutes = (endTime - startTime) / (1000 * 60 * interval);  
        if (betweenMinutes == 0) {
			return dates;
		}
        
        for (int i = 1; i < betweenMinutes; i++) {
        	calendar.add(Calendar.MINUTE, interval);
        	dates.add(calendar.getTime());
		}
        
        dates.add(endDate);
        
        return dates;
	}
	
	public static List<String> getBetweenMinuteStrings(long startLongMinute, long endLongMinute, int interval, String sformat, String tformat) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		
		Calendar calendar = Calendar.getInstance(); 
		
		int startMinute = (int)(startLongMinute - startLongMinute / 10 * 10);
		int endMinute = (int)(endLongMinute - endLongMinute / 10 * 10);
		
		if (startMinute > 0 && startMinute < 5) {
			startMinute = 5;
		}
		else if (startMinute > 5) {
			startMinute = 10;
		}
		
		if (endMinute > 0 && endMinute < 5) {
			endMinute = 0;
		}
		else if (endMinute > 5) {
			endMinute = 5;
		}
		
		startLongMinute = startLongMinute / 10 * 10;
		endLongMinute = endLongMinute / 10 * 10;
		
		Date endDate = formatter.parse(String.valueOf(endLongMinute));
		calendar.setTime(endDate);  
		calendar.add(Calendar.MINUTE, endMinute);
		endDate = calendar.getTime();
        long endTime = calendar.getTimeInMillis(); 
        
        Date startDate = formatter.parse(String.valueOf(startLongMinute));
        calendar.setTime(startDate);  
        calendar.add(Calendar.MINUTE, startMinute);
        startDate = calendar.getTime();
        long startTime = calendar.getTimeInMillis();  
        

        formatter = new SimpleDateFormat(tformat);
        
        List<String> dates = new ArrayList<String>();
        dates.add(formatter.format(startDate));
        
        long betweenMinutes = (endTime - startTime) / (1000 * 60 * interval);  
        if (betweenMinutes == 0) {
			return dates;
		}
        
        for (int i = 1; i < betweenMinutes; i++) {
        	calendar.add(Calendar.MINUTE, interval);
        	dates.add(formatter.format(calendar.getTime()));
		}
        
        dates.add(formatter.format(endDate));
        
        return dates;
	}
	
	public static List<String> getBetweenMinuteStrings(long startLongMinute, long endLongMinute, int interval) throws ParseException {
		return getBetweenMinuteStrings(startLongMinute, endLongMinute, interval, "yyyyMMddHHmm", "yyyyMMddHHmm");
	}
	
	public static String toMinuteOn05String(String minute, String sformat, String tformat) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(sformat);
		Date date = formatter.parse(String.valueOf(minute));
		formatter = new SimpleDateFormat("yyyyMMddHHmm");
		minute = formatter.format(date);
		long longMinute = Long.valueOf(minute);
		
		int endMinute = (int)(longMinute - longMinute / 10 * 10);
		
		if (endMinute > 0 && endMinute < 5) {
			endMinute = 0;
		}
		else if (endMinute > 5) {
			endMinute = 5;
		}
		
		longMinute = longMinute / 10 * 10;
		
		date = formatter.parse(String.valueOf(longMinute));
		
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date); 
		calendar.add(Calendar.MINUTE, endMinute);
		date = calendar.getTime();
		
		formatter = new SimpleDateFormat(tformat);
		return formatter.format(date);
	}
	
	public static void main(String[] args) throws ParseException {
		/*List<Date> minutes = getBetweenMinutes(201412011201L, 201412011216L, 5);
		System.out.println(minutes);
		
		minutes = getBetweenMinutes(201412011207L, 201412011216L, 5);
		System.out.println(minutes);
		
		minutes = getBetweenMinutes(201412011201L, 201412011214L, 5);
		System.out.println(minutes);
		
		minutes = getBetweenMinutes(201412011207L, 201412011214L, 5);
		System.out.println(minutes);
		
		minutes = getBetweenMinutes(201412040440L, 201412040600L, 5);
		System.out.println(minutes);*/
		
		List<Date> minutes = getBetweenMinutes(201412290815L, 201412291000L, 60);
		System.out.println(minutes);
		
		/*minutes = getBetweenMinutes(201412290815L, 201412291000L, 60);
		System.out.println(minutes);*/
		
		/*String date = DateUtils.getNowMinuteString();
		String mtime = DateUtils.plusMinuteString(date, -5);
		
		System.out.println(date);
		System.out.println(mtime);
		
		mtime = DateUtils.toMinuteOn05String("2014-12-16 14:42", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm");
		System.out.println(mtime);
		
		mtime = DateUtils.toMinuteOn05String("2014-12-16 14:47", "yyyy-MM-dd HH:mm", "yyyyMMddHHmm");
		System.out.println(mtime);
		
		mtime = DateUtils.toMinuteOn05String("2014-12-16 14:45", "yyyy-MM-dd HH:mm", "yyyyMMddHHmm");
		System.out.println(mtime);*/
		
		List<String> months = getBetweenMonthStrings(201410, 201412, 1);
		System.out.println(months);
		
		String month = toLastMonthString("201410");
		System.out.println(month);
	}

}
