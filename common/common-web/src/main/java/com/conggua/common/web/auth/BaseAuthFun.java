package com.conggua.common.web.auth;

import com.conggua.common.web.constant.DateFormatterConstant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ky
 * @description spEl表达式匹配类
 * @date 2024-05-27 16:52
 */
public class BaseAuthFun {

    public Boolean hasRole(String role) {
        return false;
    }

    public Boolean hasRoles(String... role) {
        return false;
    }

    public Boolean hasAnyRole(String... role) {
        return false;
    }

    public Boolean hasPermission(String permission) {
        return false;
    }

    public Boolean hasPermissions(String... permission) {
        return false;
    }

    public Boolean hasAnyPermission(String... permission) {
        return false;
    }

    public Boolean betweenDateTime(String startTime, String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(DateFormatterConstant.DEFAULT_DATE_TIME_FORMAT));
        LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern(DateFormatterConstant.DEFAULT_DATE_TIME_FORMAT));
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(start) && now.isBefore(end);
    }

    public Boolean betweenDate(String startTime, String endTime) {
        LocalDate start = LocalDate.parse(startTime, DateTimeFormatter.ofPattern(DateFormatterConstant.DEFAULT_DATE_FORMAT));
        LocalDate end = LocalDate.parse(endTime, DateTimeFormatter.ofPattern(DateFormatterConstant.DEFAULT_DATE_FORMAT));
        LocalDate now = LocalDate.now();
        return now.isAfter(start) && now.isBefore(end);
    }

    public Boolean betweenTime(String startTime, String endTime) {
        LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern(DateFormatterConstant.DEFAULT_TIME_FORMAT));
        LocalTime end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern(DateFormatterConstant.DEFAULT_TIME_FORMAT));
        LocalTime now = LocalTime.now();
        return now.isAfter(start) && now.isBefore(end);
    }
}
