package com.conggua.springboot3.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ky
 * @description 用户类型：区分后台与移动端(全系统共用 sys_user 表)
 * @date 2026-11-10 10:00
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    ADMIN(0, "后台"),
    MOBILE(1, "移动端");

    private final Integer type;
    private final String description;
}
