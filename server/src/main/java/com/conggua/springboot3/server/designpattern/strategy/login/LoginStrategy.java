package com.conggua.springboot3.server.designpattern.strategy.login;

import com.conggua.common.base.util.JwtUtils;
import com.conggua.common.base.util.SpringContextUtils;
import com.conggua.common.base.util.UUIDUtils;
import com.conggua.common.redis.util.RedisUtils;
import com.conggua.springboot3.server.constant.LoginConstant;
import com.conggua.springboot3.server.constant.RedisKey;
import com.conggua.springboot3.server.model.bo.UserInfo;
import com.conggua.springboot3.server.model.dto.UserLoginDTO;
import com.conggua.springboot3.server.model.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ky
 * @description
 * @date 2024-08-23 14:43
 */
public interface LoginStrategy {

    /**
     * 执行逻辑
     * @param dto
     * @return
     */
    Map<String, String> execute(UserLoginDTO dto);

    /**
     * 生成token
     * @param user
     * @return
     */
    default Map<String, String> generateToken(User user) {
        String userId = user.getId();
        // 生成设备指纹
        String deviceFingerprint = UUIDUtils.getUuid();
        // 生成 AccessToken 和 RefreshToken
        Map<String, String> claimMap = Map.of("userId", userId, "deviceFingerprint", deviceFingerprint);
        String accessToken = JwtUtils.generateToken(claimMap, true);
        String refreshToken = JwtUtils.generateToken(claimMap, false);
        // 存储到 redis
        RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId, deviceFingerprint), accessToken, LoginConstant.ACCESS_TOKEN_EXPIRE, TimeUnit.MINUTES);
        RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId, deviceFingerprint), refreshToken, LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);

        // 保存用户信息到 redis
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        userInfo.setDeviceFingerprint(deviceFingerprint);
        RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.USER_INFO, userId), userInfo, LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    /**
     * 存储 RefreshToken 到 Cookie
     * @param refreshToken
     */
    default void setRTCookie(String refreshToken) {
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/api/user/renewal");
        refreshCookie.setMaxAge((int) (LoginConstant.REFRESH_TOKEN_EXPIRE * 24 * 3600));
        HttpServletResponse response = SpringContextUtils.getHttpServletResponse();
        response.addHeader("Set-Cookie",
                String.format("refreshToken=%s; HttpOnly; Secure; SameSite=Strict; Path=/api/user/renewal; Max-Age=%d",
                        refreshToken, LoginConstant.REFRESH_TOKEN_EXPIRE * 24 * 3600));
    }
}
