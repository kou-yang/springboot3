package com.conggua.common.base.util;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author ky
 * @description LocalDate具类
 * @date 2024-04-15 16:06
 */
public class LocalDateUtils {

    /**
     * Date 转 LocalDateTime
     *
     * @param time 时间
     * @return LocalDateTime
     */
    public LocalDateTime toLocalDateTime(Date time) {
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 秒级、毫秒级时间戳转 LocalDateTime
     *
     * @param epoch 时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(long epoch) {
        if (String.valueOf(epoch).length() == 10) {
            return Instant.ofEpochSecond(epoch).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        return Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Date 转 LocalDate
     *
     * @param time 时间
     * @return LocalDate
     */
    public LocalDate toLocalDate(Date time) {
        return time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 毫秒级时间戳转 LocalDate
     *
     * @param epoch 秒级或毫秒级时间戳
     * @return LocalDate
     */
    public static LocalDate toLocalDate(long epoch) {
        if (String.valueOf(epoch).length() == 10) {
            Instant.ofEpochSecond(epoch).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * LocalDateTime 转毫秒级时间戳
     *
     * @param time 时间
     * @return 毫秒级时间戳
     */
    public static long toEpochMilli(LocalDateTime time) {
        return time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * LocalDate 转毫秒级时间戳
     *
     * @param time 时间
     * @return 毫秒级时间戳
     */
    public static long toEpochMilli(LocalDate time) {
        return toEpochMilli(LocalDateTime.of(time, LocalTime.MIN));
    }

    /**
     * LocalDateTime 转秒级时间戳
     *
     * @param time 时间
     * @return 秒级时间戳
     */
    public static long toEpochSecond(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    /**
     * LocalDate 转秒级时间戳
     *
     * @param time 时间
     * @return 秒级时间戳
     */
    public static long toEpochSecond(LocalDate time) {
        return toEpochSecond(LocalDateTime.of(time, LocalTime.MIN));
    }

    /**
     * 获取 time 所在周的开始时间
     *
     * @param time 时间
     * @return time 所在周的开始时间
     */
    public static LocalDateTime getFirstDayOfWeek(LocalDateTime time) {
        int dayOfWeek = time.getDayOfWeek().getValue();
        return time.minusDays(dayOfWeek - 1).with(LocalTime.MIN);
    }

    /**
     * 获取 time 所在周的开始时间
     * @param time
     * @return
     */
    public static LocalDate getFirstDayOfWeek(LocalDate time) {
        int dayOfWeek = time.getDayOfWeek().getValue();
        return time.minusDays(dayOfWeek - 1) ;
    }

    /**
     * 获取 time 所在周的结束时间
     *
     * @param time 时间
     * @return time 所在周的结束时间
     */
    public static LocalDateTime getLastDayOfWeek(LocalDateTime time) {
        int dayOfWeek = time.getDayOfWeek().getValue();
        return time.plusDays(7 - dayOfWeek).with(LocalTime.MAX);
    }

    /**
     * 获取 time 所在周的结束时间
     * @param time
     * @return
     */
    public static LocalDate getLastDayOfWeek(LocalDate time) {
        int dayOfWeek = time.getDayOfWeek().getValue();
        return time.plusDays(7 - dayOfWeek);
    }

    /**
     * 获取 time 所在月份的开始时间
     *
     * @param time 时间
     * @return time 所在月份的开始时间
     */
    public static LocalDateTime getFirstDayOfMonth(LocalDateTime time) {
        return time.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    /**
     * 获取 time 所在月份的开始时间
     * @param time
     * @return
     */
    public static LocalDate getFirstDayOfMonth(LocalDate time) {
        return time.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取 time 所在月份的结束时间
     *
     * @param time 时间
     * @return time 所在月份的结束时间
     */
    public static LocalDateTime getLastDayOfMonth(LocalDateTime time) {
        return time.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }

    /**
     * 获取 time 所在月份的结束时间
     * @param time
     * @return
     */
    public static LocalDate getLastDayOfMonth(LocalDate time) {
        return time.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取 time 所在年份的开始时间
     *
     * @param time 时间
     * @return time 所在年份的开始时间
     */
    public static LocalDateTime getFirstDayOfYear(LocalDateTime time) {
        return time.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
    }

    /**
     * 获取 time 所在年份的开始时间
     * @param time
     * @return
     */
    public static LocalDate getFirstDayOfYear(LocalDate time) {
        return time.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 获取 time 所在年份的结束时间
     *
     * @param time 时间
     * @return time 所在年份的结束时间
     */
    public static LocalDateTime getLastDayOfYear(LocalDateTime time) {
        return time.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
    }

    /**
     * 获取 time 所在年份的结束时间
     * @param time
     * @return
     */
    public static LocalDate getLastDayOfYear(LocalDate time) {
        return time.with(TemporalAdjusters.lastDayOfYear());
    }
}
