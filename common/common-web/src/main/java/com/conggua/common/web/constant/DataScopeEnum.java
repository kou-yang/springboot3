package com.conggua.common.web.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ky
 * @description
 * @date 2024-08-01 15:35
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    ALL("all", "所有数据权限"),
    DEPART_CHILDREN("depart_children", "本部门及以下数据权限"),
    DEPART("depart", "本部门数据权限"),
    SELF("self", "个人数据权限"),
    CUSTOM("custom", "自定义数据权限");

    private final String code;
    private final String value;
}
