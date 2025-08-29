package com.conggua.common.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ky
 * @description 复杂权限校验注解
 * @date 2024-05-27 16:20
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuth {

    /**
     * spring El
     * hasRole('admin')
     * hasRoles('admin', 'user')
     * 详细见 BaseAuthFun 类
     * @return
     */
    String value();

    /**
     * 校验失败返回提示
     * @return
     */
    String message() default "";
}
