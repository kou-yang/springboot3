package com.conggua.common.base.util.dict;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: kouyang
 * @description: 字典转换注解
 * @date: 2025-07-17 11:09
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DynamicEnumSerializer.class)
public @interface Dict {

    /**
     * 枚举类
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * 目标字段属性名，默认自动生成(原字段名 + "Name")
     */
    String targetField() default "";

    /**
     * 是否忽略大小写(仅对String类型有效)
     */
    boolean ignoreCase() default false;

    /**
     * 获取枚举code的方法名
     */
    String codeMethod() default "getCode";

    /**
     * 获取枚举显示值的方法名
     */
    String valueMethod() default "getValue";

    /**
     * 未知状态时的默认显示值
     */
    String unknownValue() default "未知状态";
}
