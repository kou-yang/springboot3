package com.conggua.springboot3.server.constant;

/**
 * @author ky
 * @description
 * @date 2024-05-15 13:39
 */
public interface RedisKey {

    String BASE_KEY = "template:";

    /**
     * 租户ID
     */
    String TENANT_ID = "%s:";

    /**
     * 用户AccessToken
     */
    String ACCESS_TOKEN = "accessToken:userId_%s";

    /**
     * 用户RefreshToken
     */
    String REFRESH_TOKEN = "refreshToken:userId_%s";

    /**
     * 用户信息
     */
    String USER_INFO = "userInfo:userId_%s";

    static String getKey(String key, Object... objects) {
        return String.format(BASE_KEY + TENANT_ID + key, objects);
    }

    static String getKeyNoTenant(String key, Object... objects) {
        return String.format(BASE_KEY + key, objects);
    }
}
