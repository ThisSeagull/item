package com.cq.start.tools.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Date Utility Class used to convert Strings to Dates and Timestamps
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a>
 *  to correct time pattern. Minutes should be mm not MM (MM is month).
 */
public class DateUtil {
    private static Log          log          = LogFactory.getLog(DateUtil.class);
    private static final String TIME_PATTERN = "HH:mm";

    /**
     * Checkstyle rule: utility classes should not have public constructor
     */
    public DateUtil() {
    }

    //Timestamp和String之间转换的函数：
    public static String getTimestampToString(Timestamp obj) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//定义格式，不显示毫秒
        String str = df.format(obj);
        return str;
    }


    /**
     * 判断时间是不是今天
     * @param date
     * @return    是返回true，不是返回false
     */
    private static boolean isNow(Date date) {
        //当前时间
        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        //获取今天的日期
        String nowDay = sf.format(now);


        //对比的时间
        String day = sf.format(date);

        return day.equals(nowDay);

    }

    /*
     * 自定义 转换模式将Timestamp 输出
     */
    public static String getTimestampToString(String formatPattern, Timestamp obj) {
        SimpleDateFormat df = new SimpleDateFormat(formatPattern);
        String str = df.format(obj);
        return str;
    }
    
    public static String getDateToString(String formatPattern, Date obj) {
        SimpleDateFormat df = new SimpleDateFormat(formatPattern);
        String str = df.format(obj);
        return str;
    }
    
    public static String getStrToString(String formatPattern, String strDate) throws ParseException {
    	Date obj = convertStringToDate(strDate);
        SimpleDateFormat df = new SimpleDateFormat(formatPattern);
        String str = df.format(obj);
        return str;
    }

    //String转化为Timestamp:
    public static Timestamp getStringToTimestamp(String str) {
        Timestamp ts = Timestamp.valueOf(str);
        return ts;
    }

    public static Date strToDate(String str, String pattern) {
        Date dateTemp = null;
        SimpleDateFormat formater2 = new SimpleDateFormat(pattern);
        try {
            dateTemp = formater2.parse(str);
        } catch (Exception e) {
            log.error("exception in convert string to date!");
        }
        return dateTemp;
    }

    /**
     * Return default datePattern (yyyy-MM-dd)
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return "yyyy-MM-dd";
    }

    public static String getDateTimePattern() {
        return DateUtil.getDatePattern() + " HH:mm:ss.S";
    }
    
    public static String getYearMonthPattern() {
        return "yyyyMM";
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to yyyy-MM-dd.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static String getDate(Date aDate) {
        SimpleDateFormat df;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }
    
    public static String getCurMonth() {
    	Date aDate = new Date();
        SimpleDateFormat df;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getYearMonthPattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }
    
    public static String getPreMonth() {
    	Date aDate = new Date();
    	Calendar calendar = Calendar.getInstance();   
    	calendar.setTime(aDate);
    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        SimpleDateFormat df;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getYearMonthPattern());
            returnValue = df.format(calendar.getTime());
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see SimpleDateFormat
     * @throws ParseException when String doesn't match the expected format
     */
    public static Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df;
        Date date;
        df = new SimpleDateFormat(aMask);

//        if (log.isDebugEnabled()) {
//            log.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
//        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format:
     * yyyy-MM-dd HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(TIME_PATTERN, theTime);
    }

    /**
     * This method returns the current date in the format: yyyy-MM-dd
     *
     * @return the current date
     * @throws ParseException when String doesn't match the expected format
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    public static Calendar getCurrentDay() throws ParseException {
        Calendar cal = Calendar.getInstance();
        return cal;
    }
    
    public static Date getTodayDate(){
    	SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
    	try {
			return convertStringToDate(df.format(new Date()));
		} catch (ParseException e) {
			return new Date();
		} 
    }
    
    public static String getTodayTimeStr(){
    	SimpleDateFormat df = new SimpleDateFormat(getDatePattern());
    	return df.format(new Date());
    }
    
    public static String getTodayDateStr(String pattern){
    	SimpleDateFormat df = new SimpleDateFormat(pattern);
    	return df.format(new Date());
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     *
     * @see SimpleDateFormat
     */
    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     *
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     *
     * @param strDate the date to convert (in format yyyy-MM-dd)
     * @return a date object
     * @throws ParseException when String doesn't match the expected format
     */
    public static Date convertStringToDate(String strDate) throws ParseException {
        Date aDate = null;

        try {
            aDate = convertStringToDate(getDatePattern(), strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate + "' to a date, throwing exception");
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return aDate;
    }
    
    
    public static Date convertStringToDateTime(String strDate) throws ParseException {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + getDatePattern());
            }

            aDate = convertStringToDate(getDateTimePattern(), strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate + "' to a date, throwing exception");
            log.error(pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return aDate;
    }

    /**
     *
     * @param aDate
     * @return
     */
    public static String convertDateToString(String pattern, Date aDate) {
        return getDateTime(pattern, aDate);
    }
    
    public static String getSpecDateString(Date aDate) {
    	if (aDate == null){
    		return null;
    	}
    	return getDateTime("yyyy/MM/dd", aDate) + " 00:00:00";
    }

    /**
     * 取得从startDate开始的前(正)/后(负)day天
     * @param startDate
     * @param day
     * @return
     */
    public static Date getRelativeDate(Date startDate, int day) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(startDate);
            calendar.add(Calendar.DAY_OF_MONTH, day);
            return calendar.getTime();
        } catch (Exception e) {
            log.error(e);
            return startDate;
        }
    }

    /**
     * 取得从startDate开始的前(正)/后(负)day天,的具体时间
     * @param startDate
     * @param day
     * @return
     */
    public static Date RelativeDateTime(Date startDate, int day,String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, day);
            String str=convertDateToString("yyyy-MM-dd",calendar.getTime());
            str+=" "+time;
            Date date=null;
            date=convertStringToDate("yyyy-MM-dd HH:mm:ss",str);
            return date;
        } catch (Exception e) {
            log.error(e);
            return startDate;
        }
    }

    public static Date RelativeDateTimeAfter(){
        String afterStr=DateUtil.convertDateToString("yyyy-MM-dd",new Date());
        afterStr+=" "+"18:00:00";
        Date afterDate=null;
        try {
            afterDate=DateUtil.convertStringToDate(afterStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return afterDate;
    }
    
    public static Date getJustToday() {
    	Calendar calendar = Calendar.getInstance();
        try {
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            log.error(e);
            return new Date();
        }
    }

    /**
     * 根据日期获取星期几
     *
     * @param date java.util.Date对象,不能为null
     * @return
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 统计两个时间差，返回的是天数(即24小时算一天，少于24小时就为0，用这个的时候最好把小时、分钟等去掉)
     * @param
     * @param
     * @return
     */
    public static int countDays(String beginStr, String endStr, String Foramt) {
        Date end = strToDate(endStr, Foramt);
        Date begin = strToDate(beginStr, Foramt);
        long times = end.getTime() - begin.getTime();
        return (int) (times / 60 / 60 / 1000 / 24);
    }
    
    public static int countDays(Date beginDate, Date endDate) {
        long times = endDate.getTime() - beginDate.getTime();
        return (int) (times / 60 / 60 / 1000 / 24);
    }
    
    public static boolean isDate(DateFormat df, String val) {
    	try {
    		df.parse(val);
		  } catch (ParseException e) {
			   return false;
		  }
		  return true;
    }
    
    public static boolean isAfterNow(Date ymdDate, int hour) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(ymdDate);
    	cal.add(Calendar.HOUR, hour);
    	return -1 == Calendar.getInstance().compareTo(cal);
    }
    
    public static boolean isEqualAndBeforeDate(int year, int month, int day) {
    	Calendar cal = Calendar.getInstance();
    	cal.set(year, month - 1, day);
    	cal.add(Calendar.DATE, 1);
    	return -1 == Calendar.getInstance().compareTo(cal);
    }
    
    public static boolean isEqualAndAfterDate(int year, int month, int day) {
    	Calendar cal = Calendar.getInstance();
    	cal.set(year, month - 1, day);
    	return 0 <= Calendar.getInstance().compareTo(cal);
    }
    
    /**
     * 转换成中文 年月日
     */
    public static String dateToStrLong(Date dateDate) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
    	String dateString = formatter.format(dateDate);
    	return dateString;
    }
    
	public static String getCurYear() {
		return convertDateToString("yyyy", new Date());
	}
    
//    public static void main(String[] args) {
//    	Date d = DateUtil.getNextNumMonthLastDay(1,new Date());
//    	System.out.println(DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss", d));
//	}
    
    /**
     * N月后的最后一天最后一秒
     * 0为当月
     * @param num
     * @param date
     * @return
     */
    public static Date getNextNumMonthLastDay(Integer num, Date date){
    	
    	if(num==null||num<0){
    		return null;
    	}
    	
//    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, num);//对月份进行计算,减去12个月
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date d = cal.getTime();
        
        return d;
    }
    
    public static Date getValidateDate(Integer num, Date date){
    	if(num==null||num<0){
    		return null;
    	}
    	
    	Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, num);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date d = cal.getTime();
        
        return d;
        
    }
    
    /**
     * 查找当月非双休日
     * @param year
     * @param month
     * @return
     */
    private static List<Date> getDates(int year,int month){    
        List<Date> dates = new ArrayList<Date>();    
            
        Calendar cal = Calendar.getInstance();    
        cal.set(Calendar.YEAR, year);    
        cal.set(Calendar.MONTH,  month - 1);    
        cal.set(Calendar.DATE, 1);    
            
            
        while(cal.get(Calendar.YEAR) == year &&     
                cal.get(Calendar.MONTH) < month){    
            int day = cal.get(Calendar.DAY_OF_WEEK);    
                
            if(!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)){    
                dates.add((Date)cal.getTime().clone());    
            }    
            cal.add(Calendar.DATE, 1);    
        }    
        return dates;    
    
    }
    
    public static Date getRandomWorkDay(int year, int month){
    	List<Date> dates = getDates(year, month); 
    	return dates.get(new Random().nextInt(dates.size()));
    }
    
    /***
	 * 取得当月天数 
	 * @return 当月天数 
	 */
	public static int getCurrentMonthLastDay()
	{  
	    Calendar a = Calendar.getInstance();
	    // 把日期设置为当月第一天  
	    a.set(Calendar.DATE, 1);
	    // 日期回滚一天，也就是最后一天  
	    a.roll(Calendar.DATE, -1);
	    int maxDate = a.get(Calendar.DATE);  
	    return maxDate;  
	}
	
	public static Date getOffsetDateByMonth(Date date, int m) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
    	int day = ca.get(Calendar.DATE);
    	
		ca = Calendar.getInstance();
		ca.setTime(date);
    	ca.add(Calendar.MONTH, m);
    	ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
    	int maxDay = ca.get(Calendar.DATE);
    	
    	if (day < maxDay) {
    		ca.set(Calendar.DAY_OF_MONTH, day);
    	}
    	
    	return ca.getTime();
	}
	
	/**
	 * 时间的计算
	 * @param preDate 计算前的时间
	 * @param count 要相加/相减的数字
	 * @return
	 */
	public static Date countDate(Date preDate,Integer count){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(preDate);
		calendar.add(calendar.DATE,count);//把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime();
	}
	
	/**
	 * 获取本周第一天（周一）
	 */
	public static Date getCurrentWeekFirstDay(){
		Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date first = c.getTime();
        return first;
	}
	
	/**
	 * 获取本周最后一天（周天）
	 */
	public static Date getCurrentWeekLastDay(){
		Calendar c = Calendar.getInstance();

        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Date last = c.getTime();
        return last;
	}
    
	/**
	 * 获取本月第一天
	 * @return
	 */
	public static Date getCurrentMonthFirstDate(){
		Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
       
        return c.getTime();
	}
	
	/**
	 * 获取本月最后一天
	 * @return
	 */
	public static Date getCurrentMonthLastDate(){
		Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        return ca.getTime();
	}
	
	/** 
     * 获取当年的第一天 
     * @param
     * @return 
     */  
	public static Date getCurrYearFirst(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearFirst(currentYear);  
    } 
	
	/** 
     * 获取当年的最后一天 
     * @param
     * @return 
     */  
    public static Date getCurrYearLast(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearLast(currentYear);  
    }  
      
    /** 
     * 获取某年第一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getYearFirst(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        Date currYearFirst = calendar.getTime();  
        return currYearFirst;  
    }  
      
    /** 
     * 获取某年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getYearLast(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
          
        return currYearLast;  
    }  
    
    /**
     * 获取某年某月最后一天
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(Integer year,Integer month)  
    {  
        Calendar cal = Calendar.getInstance();  
        //设置年份  
        cal.set(Calendar.YEAR,year);  
        //设置月份  
        cal.set(Calendar.MONTH, month-1);  
        //获取某月最大天数  
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
        //设置日历中月份的最大天数  
        cal.set(Calendar.DAY_OF_MONTH, lastDay);  
        //格式化日期  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String lastDayOfMonth = sdf.format(cal.getTime());  
          
        return lastDayOfMonth;  
    }  
    
    /**
     * 获取某年某月第一天
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(Integer year,Integer month)  
    {  
        String yearStr = year.toString();
        
        String monthStr = month.toString();

        if(month<10){
        	monthStr = "0" + monthStr;
        }
        
        return yearStr + "-" +  monthStr + "-01";  
    }  
	
    /**
     * 获取某年的第几周的开始日期
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
	     Calendar c = new GregorianCalendar();
	     c.set(Calendar.YEAR, year);
	     c.set(Calendar.MONTH, Calendar.JANUARY);
	     c.set(Calendar.DATE, 1);
	
	     Calendar cal = (GregorianCalendar) c.clone();
	     cal.add(Calendar.DATE, week * 7);
	
	     return getFirstDayOfWeek(cal.getTime());
    }
    
    /**
     * 获取某年的第几周的结束日期
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
	     Calendar c = new GregorianCalendar();
	     c.set(Calendar.YEAR, year);
	     c.set(Calendar.MONTH, Calendar.JANUARY);
	     c.set(Calendar.DATE, 1);
	
	     Calendar cal = (GregorianCalendar) c.clone();
	     cal.add(Calendar.DATE, week * 7);
	
	     return getLastDayOfWeek(cal.getTime());
    }
    
    /**
     * 获取当前时间所在周的开始日期
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
	     Calendar c = new GregorianCalendar();
	     c.setFirstDayOfWeek(Calendar.MONDAY);
	     c.setTime(date);
	     c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
	     return c.getTime();
    }

    /**
     * 获取当前时间所在周的结束日期
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
	     Calendar c = new GregorianCalendar();
	     c.setFirstDayOfWeek(Calendar.MONDAY);
	     c.setTime(date);
	     c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
	     return c.getTime();
    }
    public static int diffHour(Date date){
        long from = 0;
        long to = 0;
        from = date.getTime();
        to = new Date().getTime();
        int hours = (int) ((to - from) / (1000 * 60 * 60));
        return hours;
    }
    public static long getDatePoor(Date endDate, Date nowDate) {

        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day;
    }
    
    public static boolean isNowBetween(Date start, Date end, boolean isDate) {
    	boolean flag = true;
    	
    	Calendar now = Calendar.getInstance();  
    	if (isDate) {
	    	now.set(Calendar.HOUR_OF_DAY, 0);  
	    	now.set(Calendar.MINUTE, 0);  
	    	now.set(Calendar.SECOND, 0);  
	    	now.set(Calendar.MILLISECOND, 0); 
    	}
        
    	if (null != start) {
    		Calendar cal = Calendar.getInstance();
        	cal.setTime(start);
        	if (isDate) {
        		cal.set(Calendar.HOUR_OF_DAY, 0);
            	cal.set(Calendar.MINUTE, 0);
            	cal.set(Calendar.SECOND, 0);
            	cal.set(Calendar.MILLISECOND, 0);
        	}
            flag = 0 <= now.compareTo(cal);
    	}
    	
    	if (flag && null != end) {
    		Calendar cal = Calendar.getInstance();
        	cal.setTime(end);
        	if (isDate) {
        		cal.set(Calendar.HOUR_OF_DAY, 0);
            	cal.set(Calendar.MINUTE, 0);
            	cal.set(Calendar.SECOND, 0);
            	cal.set(Calendar.MILLISECOND, 0);
        	}
            flag = 0 >= now.compareTo(cal);
    	}
    	
    	return flag;
    }
    
    public static boolean isDateBetween(Date start, Date end, Date date, boolean isDate) {
    	boolean flag = true;
    	
    	Calendar now = Calendar.getInstance();  
    	now.setTime(date);
    	
    	if (isDate) {
	    	now.set(Calendar.HOUR_OF_DAY, 0);  
	    	now.set(Calendar.MINUTE, 0);  
	    	now.set(Calendar.SECOND, 0);  
	    	now.set(Calendar.MILLISECOND, 0); 
    	}
        
    	if (null != start) {
    		Calendar cal = Calendar.getInstance();
        	cal.setTime(start);
        	if (isDate) {
        		cal.set(Calendar.HOUR_OF_DAY, 0);
            	cal.set(Calendar.MINUTE, 0);
            	cal.set(Calendar.SECOND, 0);
            	cal.set(Calendar.MILLISECOND, 0);
        	}
            flag = 0 <= now.compareTo(cal);
    	}
    	
    	if (flag && null != end) {
    		Calendar cal = Calendar.getInstance();
        	cal.setTime(end);
        	if (isDate) {
        		cal.set(Calendar.HOUR_OF_DAY, 0);
            	cal.set(Calendar.MINUTE, 0);
            	cal.set(Calendar.SECOND, 0);
            	cal.set(Calendar.MILLISECOND, 0);
        	}
            flag = 0 >= now.compareTo(cal);
    	}
    	
    	return flag;
    }
    
    /***
     * 如果date1 > date2返回1，= 返回0，小于返回-1
     * @param date1
     * @param date2
     * @param isDate
     * @return
     */
    public static int compare(Date date1, Date date2, boolean isDate) {
    	Calendar date1Cal = Calendar.getInstance();  
    	date1Cal.setTime(date1);

    	if (isDate) {
    		date1Cal.set(Calendar.HOUR_OF_DAY, 0);  
    		date1Cal.set(Calendar.MINUTE, 0);  
    		date1Cal.set(Calendar.SECOND, 0);  
    		date1Cal.set(Calendar.MILLISECOND, 0); 
    	}
    	
    	Calendar date2Cal = Calendar.getInstance();  
    	date2Cal.setTime(date2);

    	if (isDate) {
    		date2Cal.set(Calendar.HOUR_OF_DAY, 0);  
    		date2Cal.set(Calendar.MINUTE, 0);  
    		date2Cal.set(Calendar.SECOND, 0);  
    		date2Cal.set(Calendar.MILLISECOND, 0); 
    	}
        
    	return date1Cal.compareTo(date2Cal);
    }

    public static  Date getDateByTimeStamp(String TimeStamp){
        Timestamp ts = new Timestamp(Long.parseLong(TimeStamp));
        Date date = new Date();
        try {
            date = ts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    
    public static Date dateFormat(Date date, String pattern){
    	SimpleDateFormat df;
        Date newDate = null;
        df = new SimpleDateFormat(pattern);
    	String datestr = df.format(date);
    	try {
			newDate = df.parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newDate;
    	
    }
    public static void main(String[] args) throws ParseException {
//    	List<Date> dates = getDates(2016, 7);
//    	for(Date date:dates){
//    		System.out.println(dates.get(new Random().nextInt(dates.size())));
//    	}
    	
//    	System.out.println(DateUtil.getValidateDate(12, DateUtil.convertStringToDate("yyyy-MM-dd", "2016-10-01")));
    	
//    	System.out.println(DateUtil.countDays(new Date(), new Date()));
//    	System.out.println(DateUtil.getTodayDate());
//    	System.out.println(DateUtil.countDate(new Date(), 5));
//      System.out.println(DateUtil.getValidateDate(12, DateUtil.convertStringToDate("yyyy-MM-dd", "2016-10-01")));
//    	System.out.println(DateUtil.getCurrentMonthFirstDate());
//    	System.out.println(DateUtil.getCurrentMonthLastDate());
    	
    	System.out.println(DateUtil.getFirstDayOfWeek(2015, 52));
    	System.out.println("1510745143535");
    	System.out.println(Integer.MAX_VALUE);
    	System.out.println(getStrToString("yyyy.MM.dd", "2017-12-20 11:11:11"));
    	
    	Date s = new Date();
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(s);
    	cal.add(Calendar.DATE, -1);
    	
    	Date e = new Date();
    	Calendar cal2 = Calendar.getInstance();
    	cal2.setTime(e);
    	cal2.add(Calendar.DATE, -1);
    	
    	System.out.println(isNowBetween(cal.getTime(), cal2.getTime(), true));
    	
    	System.out.println(new SimpleDateFormat("yyyyMMdd").format(new Date()));
	}
    
}
