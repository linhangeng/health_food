package com.example.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @ClassName LocalDateTimeUtils
 * @Author sheng.lin
 * @Date 2025/7/18
 * @Version 1.0
 * @修改记录 使用场景：需要精确到时分秒
 **/
public class LocalDateTimeUtils {

    // 常用格式
    public static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter YMD_HMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter YMD_HMS_SSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * 获取当前时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * LocalDateTime 转字符串
     */
    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }

    /**
     * 字符串转 LocalDateTime
     */
    public static LocalDateTime parse(String text, DateTimeFormatter formatter) {
        return LocalDateTime.parse(text, formatter);
    }

    /**
     * 时间戳转 LocalDateTime（毫秒）
     */
    public static LocalDateTime fromMillis(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime 转时间戳（毫秒）
     */
    public static long toMillis(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * LocalDateTime 加减时间
     */
    public static LocalDateTime plus(LocalDateTime time, long amountToAdd, ChronoUnit unit) {
        return time.plus(amountToAdd, unit);
    }

    public static LocalDateTime minus(LocalDateTime time, long amountToSubtract, ChronoUnit unit) {
        return time.minus(amountToSubtract, unit);
    }

    /**
     * 获取两个时间之间的间隔（单位可选）
     */
    public static long between(LocalDateTime start, LocalDateTime end, ChronoUnit unit) {
        return unit.between(start, end);
    }

    /**
     * 获取当天的开始时间（00:00:00）
     */
    public static LocalDateTime startOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atStartOfDay();
    }

    /**
     * 获取当天的结束时间（23:59:59.999）
     */
    public static LocalDateTime endOfDay(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTimeUtils.now();
        System.out.println("当前时间: " + LocalDateTimeUtils.format(now, LocalDateTimeUtils.YMD_HMS));

        String dateStr = "2025-07-18 12:30:00";
        LocalDateTime parsed = LocalDateTimeUtils.parse(dateStr, LocalDateTimeUtils.YMD_HMS);
        System.out.println("解析后时间: " + parsed);

        long millis = LocalDateTimeUtils.toMillis(parsed);
        System.out.println("转换为时间戳: " + millis);

        LocalDateTime fromMillis = LocalDateTimeUtils.fromMillis(millis);
        System.out.println("时间戳还原: " + fromMillis);

        LocalDateTime tomorrow = LocalDateTimeUtils.plus(now, 1, ChronoUnit.DAYS);
        System.out.println("明天: " + LocalDateTimeUtils.format(tomorrow, LocalDateTimeUtils.YMD_HMS));

        long hours = LocalDateTimeUtils.between(now, tomorrow, ChronoUnit.HOURS);
        System.out.println("相差小时: " + hours);
    }
}
