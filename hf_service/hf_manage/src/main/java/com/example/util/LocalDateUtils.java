package com.example.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName DateUtils
 * @Author sheng.lin
 * @Date 2025/7/18
 * @Version 1.0
 * @修改记录 只需要关注日期
 **/
public class LocalDateUtils {

    private LocalDateUtils() { /* 禁用实例化 */ }

    /**
     * 默认的日期格式化器：yyyy-MM-dd
     */
    private static final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE;


    /**
     * 获取今天的日期
     *
     * @return LocalDate.now()
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 将字符串按 "yyyy-MM-dd" 解析成 LocalDate
     *
     * @param text 日期字符串
     * @param formatter 日期格式化器
     * @return LocalDate
     */
    public static LocalDate parseIso(String text,DateTimeFormatter formatter) {
        return LocalDate.parse(text, formatter);
    }


    /**
     * 将 LocalDate 格式化成 "yyyy-MM-dd" 字符串
     *
     * @param date 要格式化的日期
     * @return 格式化结果
     */
    public static String formatIso(LocalDate date) {
        return date.format(ISO_LOCAL_DATE);
    }

    /**
     * 计算两个日期之间的完整年数（常用来算年龄）
     *
     * @param birth 出生日期
     * @param now   参考日期（可传 today()）
     * @return 年数
     */
    public static int yearsBetween(LocalDate birth, LocalDate now) {
        // JDK 8 ~ JDK 17 都有 Period.between，但 JDK 17 可以更直接地只取 years()
        return Period.between(birth, now).getYears();
    }

    /**
     * 获取给定日期是一年中的第几季度
     *
     * @param date LocalDate
     * @return 1..4
     */
    public static int quarter(LocalDate date) {
        // JDK 17 支持 switch 表达式，结合 Month 枚举非常清晰
        return switch (date.getMonth()) {
            case JANUARY, FEBRUARY, MARCH -> 1;
            case APRIL, MAY, JUNE -> 2;
            case JULY, AUGUST, SEPTEMBER -> 3;
            default -> 4;
        };
    }

    /**
     * 获取 [start, end) 之间所有日期（不含 end），常用于生成报表的日期序列
     *
     * @param startInclusive 开始日期（包含）
     * @param endExclusive   结束日期（不包含）
     * @return List&lt;LocalDate&gt;
     */
    public static List<LocalDate> datesBetween(LocalDate startInclusive, LocalDate endExclusive) {
        // JDK 9 引入的 LocalDate::datesUntil，JDK 17 自带，无需自己写循环
        return startInclusive.datesUntil(endExclusive).collect(Collectors.toList());
    }

    /**
     * 获得下一个指定的星期几
     *
     * @param date      基准日期
     * @param targetDay 例如 DayOfWeek.MONDAY
     * @return >= date 的下一个该星期几（如果 date 本身就是，返回 date）
     */
    public static LocalDate nextOrSame(LocalDate date, DayOfWeek targetDay) {
        // JDK 17：DayOfWeek.plus 可以和 LocalDate.with 一起用，也可自写
        int currentDow = date.getDayOfWeek().getValue();
        int target = targetDay.getValue();
        int delta = (target - currentDow + 7) % 7;
        return date.plusDays(delta);
    }

    public static void main(String[] args) {
        LocalDate localDate = parseIso("2025-07-18", ISO_LOCAL_DATE);
        System.out.println(localDate.toString());
//        System.out.println(parseIso("2025-07-18", FMT_YMD_HMS));
    }
}
