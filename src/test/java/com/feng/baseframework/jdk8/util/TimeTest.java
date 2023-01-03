package com.feng.baseframework.jdk8.util;

import com.feng.baseframework.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ProjectName: baseframework
 * @Description: 时间api测试
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/14 22:54
 * @UpdateUser:
 * @UpdateDate: 2018/5/14 22:54
 * @UpdateRemark:
 * @Version: 1.0
 */
public class TimeTest {

	@Test
	public void getTimeTokenTest(){
		String[] times = DateUtil.getTimeToken(new Date());
		System.out.println(times[0]);
		System.out.println(times[1]);
		System.out.println(times[2]);
		System.out.println(times[3]);
		System.out.println(times[4]);
		System.out.println(times[5]);
		System.out.println(times[6]);
		System.out.println(times[7]);
		System.out.println(times[3]+times[4]);
	}

	@Test
	public void timePatternTest(){
		String reg = "([2]\\d{3}(((0[13578]|1[02])([0-2]\\d|3[01]))|((0[469]|11)([0-2]\\d|30))|(02([01]\\d|2[0-8])))([01][0-9]|2[0-3])([0-5]\\d)([0-5]\\d))";
		String str = "9003268435225620181029090237606509";
		Pattern pattern = Pattern.compile (reg);
		Matcher matcher = pattern.matcher (str);
		while (matcher.find ())
		{
			System.out.println (matcher.group ());
		}

		System.out.println(DateUtil.getDateByPattern(str));
	}

	@Test
	public void testCalendar(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		c.setTime(new Date());
		c.add(Calendar.DATE, -30);
		Date m = c.getTime();

		System.out.println(format.format(m));
	}

	@Test
	public void testTimeZone() {
		Date now = new Date();
		Calendar calendar8 = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		calendar8.setTime(now);

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(now);


		Assert.assertTrue("不同时区时间戳相同", calendar8.getTimeInMillis() == calendar.getTimeInMillis());
	}
}
