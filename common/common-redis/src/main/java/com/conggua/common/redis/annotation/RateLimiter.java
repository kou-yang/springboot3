package com.conggua.common.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解（滑动窗口）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimiter {

    /**
     * key的前缀（默认取方法全限定名）
     *
     * @return key 的前缀
     */
    String prefixKey() default "";

    /**
     * 滑动窗口大小（默认10）
     *
     * @return 时间常数
     */
    long time() default 10L;

    /**
     * 时间单位（默认秒）
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 窗口时间内允许最大访问次数（默认5次）
     *
     * @return 访问次数
     */
    int count() default 5;

    /**
     * 频控对象，默认IP
     *
     * @return 类型
     */
    Target target() default Target.IP;

    /**
     * spEL表达式，target = EL时必填
     *
     * @return 表达式
     */
    String spEl() default "";

    enum Target {
        IP,
        UID,
        EL
    }
}
