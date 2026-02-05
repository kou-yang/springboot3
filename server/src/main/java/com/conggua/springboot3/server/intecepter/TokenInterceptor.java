package com.conggua.springboot3.server.intecepter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.util.JwtUtils;
import com.conggua.common.redis.util.RedisUtils;
import com.conggua.springboot3.server.constant.LoginConstant;
import com.conggua.springboot3.server.constant.RedisKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author ky
 * @description 登录校验
 * @date 2024-05-15 11:48
 */
@Order(0)
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_SCHEMA = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String accessToken = getToken(request);
        DecodedJWT jwt = JwtUtils.verify(accessToken, true);
        if (Objects.isNull(jwt)) {
            CommonErrorEnum.NOT_LOGIN_ERROR.sendHttpError();
            return false;
        }
        Map<String, ?> map = JwtUtils.getMap(jwt);
        String userId = (String) map.get("userId");
        if (StringUtils.isAnyBlank(userId)) {
            CommonErrorEnum.NOT_LOGIN_ERROR.sendHttpError();
            return false;
        }
        // 查询 AccessToken 和 RefreshToken
        String accessKey = RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId);
        String refreshKey = RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId);
        String latestAccessToken = RedisUtils.get(accessKey, String.class);
        String refreshToken = RedisUtils.get(refreshKey, String.class);
        if (StringUtils.isBlank(refreshToken)) {
            // RefreshToken 过期（未登录）
            CommonErrorEnum.NOT_LOGIN_ERROR.sendHttpError();
            return false;
        }
        // 校验是否是最新的token
        if (Objects.equals(accessToken, latestAccessToken)) {
            request.setAttribute("userId", userId);
            return true;
        } else {
            CommonErrorEnum.UN_KNOWN_SESSION_ERROR.sendHttpError();
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // RefreshToken 续期
        String userId = (String) request.getAttribute("userId");
        String tenantId = (String) request.getAttribute("tenantId");
        String refreshKey = RedisKey.getKey(RedisKey.REFRESH_TOKEN, tenantId, userId);
        RedisUtils.expire(refreshKey, LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        return Optional.ofNullable(header)
                .filter(token -> token.startsWith(AUTHORIZATION_SCHEMA))
                .map(token -> token.substring(AUTHORIZATION_SCHEMA.length()))
                .orElse(null);
    }
}