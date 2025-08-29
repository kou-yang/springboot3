package com.conggua.common.base.util.sensitive;

import java.util.function.Function;

/**
 * @author ky
 * @description
 * @date 2024-05-23 14:30
 */
public enum SensitiveStrategy {

    /**
     * 姓名
     */
    NAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),

    /**
     * 身份证
     */
    ID_CARD(s -> s.replaceAll("(\\d{6})\\d*([0-9a-zA-Z]{4})", "$1******$2")),

    /**
     * 手机号
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),

    /**
     * 邮箱
     */
    EMAIL(s -> s.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2"));

    private final Function<String, String> desensitizer;

    SensitiveStrategy(Function<String, String> desensitizer) {
        this.desensitizer = desensitizer;
    }

    public String desensitize(String value) {
        return desensitizer.apply(value);
    }
}
