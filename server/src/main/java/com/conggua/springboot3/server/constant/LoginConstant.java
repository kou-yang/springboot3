package com.conggua.springboot3.server.constant;

/**
 * @author: kouyang
 * @description:
 * @date: 2026-02-02 10:47
 */
public interface LoginConstant {

    /**
     * AccessToken过期时间（15min）
     */
    Long ACCESS_TOKEN_EXPIRE = 15L;

    /**
     * RefreshToken过期时间（7day）
     */
    Long REFRESH_TOKEN_EXPIRE = 7L;
}
