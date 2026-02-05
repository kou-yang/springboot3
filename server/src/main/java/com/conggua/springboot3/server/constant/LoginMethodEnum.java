package com.conggua.springboot3.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author ky
 * @description
 * @date 2024-08-23 14:21
 */
@Getter
@AllArgsConstructor
public enum LoginMethodEnum {

    PC(0, "accountPassword", "账号密码登录");

    private final Integer type;
    private final String value;
    private final String describe;

    public static String getValueByType(Integer type) {
        return Arrays.stream(LoginMethodEnum.values())
                .filter(item -> item.getType().equals(type))
                .findFirst()
                .map(LoginMethodEnum::getValue)
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + type));
    }
}
