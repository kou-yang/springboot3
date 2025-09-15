package com.conggua.common.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-09-12 10:38
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 幂等 Key 表达式，支持SpEL，如 #request.orderNo, #userId, #args[0]
     */
    String key();

    /**
     * key 前缀
     */
    String prefix() default "idempotent:";

    /**
     * 过期时间，默认5分钟
     */
    long expire() default 5;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 提示消息
     */
    String message() default "请勿重复提交";
}
