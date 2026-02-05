package com.conggua.springboot3.server.intecepter;

import com.conggua.common.base.common.UserHolder;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.model.entity.User;
import com.conggua.common.redis.util.RedisUtils;
import com.conggua.springboot3.server.constant.RedisKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author ky
 * @description 信息收集
 * @date 2024-05-15 11:57
 */
@Order(2)
@Slf4j
@Component
public class CollectorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String userId = Optional.ofNullable(request.getAttribute("userId")).map(Object::toString).orElse(null);
        String key = RedisKey.getKeyNoTenant(RedisKey.USER_INFO, userId);
        // 查询用户信息
        User user = RedisUtils.get(key, User.class);
        if (Objects.isNull(user)) {
            CommonErrorEnum.NOT_LOGIN_ERROR.sendHttpError();
            return false;
        }
        UserHolder.set(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserHolder.remove();
    }
}