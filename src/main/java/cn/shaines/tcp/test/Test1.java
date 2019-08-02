package cn.shaines.tcp.test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author houyu
 * @createTime 2019/8/2 9:12
 */
public class Test1 {

    public static void main(String[] args) {


        /** Instant代替Date */
        Instant instant = Instant.now();
        long epochMilli = instant.toEpochMilli();// 毫秒值
        System.out.println("epochMilli = " + epochMilli);
        System.out.println("epochMilli = " + System.currentTimeMillis());
        long epochSecond = instant.getEpochSecond();// 秒值
        System.out.println("epochSecond = " + epochSecond);
        // Instant 转 Date
        Date date = Date.from(instant);
        // Date 转 Instant
        Instant instant1 = date.toInstant();
        /** LocalDateTime 代替 Calendar */
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = localDateTime.plusDays(1);// 加一天
        LocalDateTime localDateTime2 = localDateTime.plusDays(1);// 减一天
        // LocalDateTime 转 Instant
        Instant instant2 = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        String format = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        System.out.println("format = " + format);
        format = localDateTime.format(DateTimeFormatter.ISO_DATE);
        System.out.println("format = " + format);






        /*Instant instant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        long epochMilli = instant.toEpochMilli();
        System.out.println("epochMilli = " + epochMilli);
        System.out.println("epochMilli = " + System.currentTimeMillis());*/








    }

}
