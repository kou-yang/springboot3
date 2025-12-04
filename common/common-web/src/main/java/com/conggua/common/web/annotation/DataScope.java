package com.conggua.common.web.annotation;

import com.conggua.common.web.provider.DataScopeProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: kouyang
 * @description: 数据权限注解，在需要对数据权限控制的方法上使用次注解，使用前需创建一个{@link DataScopeProvider}实现类并重写所有方法，子类需要由Spring管理
 * @date: 2025-11-04 17:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {

    /**
     * 表名称
     */
    String tableName() default "";

    /**
     * 部门id字段名称
     */
    String departField() default "depart_id";

    /**
     * 用户id字段名称
     */
    String userField() default "create_by";
}
