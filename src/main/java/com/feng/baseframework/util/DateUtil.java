package com.feng.baseframework.util;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * baseframework
 * 2018/10/10 18:03
 * 时间工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class DateUtil {

	static DateFormat format;
	public static String DEFAULT_FORMATE = "yyyy-MM-dd HH:mm:ss";
	public static String STAMP_FORMATE = "yyyy-MM-dd HH:mm:ss,SSS";
	static{
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone timeZone = TimeZone.getTimeZone("GMT+8:00");
		format.setTimeZone(timeZone);
	}

	/**
	 * 返回数组，分别为yyyy,mm,dd,hh,mi,ss,weekday
	 * token[0] -- yyyy
	 * token[1] -- mm
	 * token[2] -- dd
	 * token[3] -- hh
	 * token[4] -- mi
	 * token[5] -- ss
	 * token[6] -- weekday
	 * token[7] -- week
	 * @param date
	 * @return 日期各部分数组
	 */
	public static String[] getTimeToken(Date date) {
		String dateStr = format.format(date);
		System.out.println(dateStr);

		String[] token = new String[8];
		String[] parts = dateStr.split(" ");
		String[] ymd = parts[0].split("-");
		String[] hms = parts[1].split(":");
		token[0] = ymd[0]; // yyyy
		token[1] = ymd[1]; // mm
		token[2] = ymd[2]; // dd
		token[3] = hms[0]; // hh
		token[4] = hms[1]; // mi
		token[5] = hms[2]; // ss

		Calendar c = Calendar.getInstance();
		//设置时区
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		c.setTime(date);

		token[6] = "" + c.get(Calendar.DAY_OF_WEEK); // week day
		token[7] = "" + c.get(Calendar.WEEK_OF_YEAR); // week of year
		return token;
	}

	/*
	 * 将时间转换为时间戳
	 */
	public static String dateToStamp(String s) throws ParseException {
		String res;
		Date date = format.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}
	/*
	 * 将时间转换为时间戳
	 */
	public static long dateToStampRlong(String s) throws ParseException{
		Date date = format.parse(s);
		long ts = date.getTime();
		return ts;
	}
	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s){
		String res;
		long lt = new Long(s);
		Date date = new Date(lt);
		res = format.format(date);
		return res;
	}
	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(long s){
		String res;
		Date date = new Date(s);
		res = format.format(date);
		return res;
	}

	/*
	 * 从字符串中提取时间
	 * 格式为yyyyMMddHHmmss
	 */
	public static String getDateByPattern(String time){
		String targetTime = "";
		String reg = "([2]\\d{3}(((0[13578]|1[02])([0-2]\\d|3[01]))|((0[469]|11)([0-2]\\d|30))|(02([01]\\d|2[0-8])))([01][0-9]|2[0-3])([0-5]\\d)([0-5]\\d))";
		Pattern pattern = Pattern.compile (reg);
		Matcher matcher = pattern.matcher (time);
		while (matcher.find ()) {
			String timeTemp = matcher.group();
			if(StringUtils.isNotBlank(timeTemp)){
				targetTime = timeTemp;
				break;
			}
		}
		return targetTime;
	}

	public static String dateToString(Date date){
		return format.format(date);
	}

	public static String dateToString(Date date, String format, TimeZone timeZone){
		if(StringUtils.isBlank(format)){
			format = DEFAULT_FORMATE;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		if(timeZone != null){
			dateFormat.setTimeZone(timeZone);
		}
		return dateFormat.format(date);
	}

	public static String now(){
		return format.format(new Date());
	}

	public static Instant getCurrentInstant(){
		Instant current = Clock.system(ZoneId.of(TimeZone.getDefault().getID())).instant();
		return current;
	}

	public static Instant getAddInstant(long amountToAdd, ChronoUnit chronoUnit){
		Instant current = Clock.system(ZoneId.of(TimeZone.getDefault().getID())).instant();
		return current.plus(amountToAdd, chronoUnit);
	}

	public static Instant getInstantByDefaultPattern(String dateString){
		Instant current = getInstantByDefaultPattern(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;
		return current;
	}

	public static Instant getInstantByDefaultPattern(String dateString,DateTimeFormatter formatter){
		Instant current = LocalDateTime.parse(dateString, formatter).atZone(ZoneId.of(TimeZone.getDefault().getID())).toInstant();
		return current;
	}

	public static long getTimeMillis(Instant instant){
		return instant.toEpochMilli();
	}

    public static String instantToString(Instant instant){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf.format(LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId()));
    }

	public static void main(String[] args) {
		Date date = new Date();
		String[] times = getTimeToken(new Date(date.getTime()/1000));
		System.out.println(times[6]);
        System.out.println(stampToDate(getCurrentInstant().toEpochMilli()));

        System.out.println(getCurrentInstant().toEpochMilli());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.ofInstant(getCurrentInstant(), TimeZone.getDefault().toZoneId())));
        System.out.println(stampToDate(getCurrentInstant().toEpochMilli()));
        System.out.println(instantToString(getCurrentInstant()));
	}
}
