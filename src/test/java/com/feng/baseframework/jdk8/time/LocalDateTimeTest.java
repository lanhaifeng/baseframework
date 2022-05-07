package com.feng.baseframework.jdk8.time;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 测试LocalDateTime
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/4/21 10:30创建:LocalDateTimeTest
 * @since 1.0
 */
public class LocalDateTimeTest {

    @Test
    public void localDateTime() {
        //时间：年、月、日、时、分、秒
        LocalDateTime localDateTime = LocalDateTime.now();
        String pattern = "yyyy-MM-dd hh:mm:ss";
        //格式化输出
        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern(pattern)));

        //获取几天前的时间
        System.out.println(localDateTime.plusDays(-3).format(DateTimeFormatter.ofPattern(pattern)));

        //获取时间戳
        System.out.println(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        //字符串转时间
        System.out.println(LocalDateTime.parse("2022-02-11 23:11:12",
                DateTimeFormatter.ofPattern(pattern)).format(DateTimeFormatter.ofPattern(pattern)));

        //指定日期
        System.out.println(
                LocalDateTime.of(2022, 11, 23, 18, 6, 12)
                        .format(DateTimeFormatter.ofPattern(pattern)));

        //获取localDate localTime date Instant
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = localDateTime.toLocalTime();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        //date转LocalDateTime
        localDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        //LocalDate LocalTime转LocalDateTime
        localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
    }

    @Test
    public void localDate() {
        //时间：年、月、日
        LocalDate localDate = LocalDate.now();
        String pattern = "yyyy-MM-dd";

        //格式化输出
        System.out.println(localDate.format(DateTimeFormatter.ofPattern(pattern)));

        //获取几天前的时间
        System.out.println(localDate.plusDays(-3).format(DateTimeFormatter.ofPattern(pattern)));

        //字符串转时间
        System.out.println(LocalDateTime.parse("2022-02-11 23:11:12",
                DateTimeFormatter.ofPattern(pattern)).format(DateTimeFormatter.ofPattern(pattern)));

        //转LocalDateTime
        LocalDateTime localDateTime = localDate.atTime(LocalTime.now());

        //转Instant
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
    }

    @Test
    public void localTime() {
        //时间：时、分、秒
        LocalTime localTime = LocalTime.now();
        //和LocalDate转LocalDateTime
        LocalDateTime localDateTime = localTime.atDate(LocalDate.now());
        //格式化输出
        System.out.println(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @Test
    public void instant() {
        //时间：时间戳
        Instant instant = Instant.now();

        //秒数
        long currentSecond = instant.getEpochSecond();

        //毫秒数
        long currentMilli = instant.toEpochMilli();

        //转date
        Date date = Date.from(instant);

        //date转Instant
        Instant instant2 = date.toInstant();
    }

    @Test
    public void clock() {
        //时间：时钟
        Clock clock = Clock.systemUTC();

        // system clock zone
        Clock defaultClock = Clock.systemDefaultZone();

        clock = Clock.system(ZoneId.systemDefault());

        //转Instant
        Instant instant = clock.instant();
    }
}
