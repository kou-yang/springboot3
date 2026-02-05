package com.conggua.springboot3.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ky
 * @description
 * @date 2024-08-23 14:21
 */
@Getter
@AllArgsConstructor
public enum PermissionTypeEnum {

    MENU("menu", "菜单"),
    BUTTON("button", "按钮"),
    API("api", "接口"),
    DATA_SCOPE("dataScope", "数据权限");

    private final String type;
    private final String description;
}
