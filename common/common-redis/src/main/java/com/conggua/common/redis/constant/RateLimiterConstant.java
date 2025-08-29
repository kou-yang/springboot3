package com.conggua.common.redis.constant;

/**
 * @author ky
 * @description 限流常量
 * @date 2023-12-13 17:31
 */
public interface RateLimiterConstant {

    /**
     * 允许
     */
    int ALLOW_ABLE = 1;

    /**
     * 不允许
     */
    int UN_ALLOWED = -1;
}
