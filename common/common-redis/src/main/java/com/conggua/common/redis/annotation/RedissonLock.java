package com.conggua.common.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author ky
 * @description 分布式锁注解（Redisson）
 * @date 2023-12-15 16:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedissonLock {

    /**
     * key的前缀（默认取方法全限定名）
     *
     * @return key的前缀
     */
    String prefixKey() default "";

    /**
     * spEl表达式
     *
     * @return 表达式
     */
    String key() default "";

    /**
     * 等待锁的时间（默认-1，不等待直接失败）
     *
     * @return 等待时间
     */
    int waitTime() default -1;

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
