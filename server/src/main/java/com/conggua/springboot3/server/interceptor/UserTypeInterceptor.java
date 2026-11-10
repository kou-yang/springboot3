package com.conggua.springboot3.server.interceptor;

import com.conggua.common.base.common.UserHolder;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.model.entity.User;
import com.conggua.springboot3.server.constant.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Objects;

/**
 * @author ky
 * @description 端类型校验：后台用户不能访问移动端接口，移动端用户不能访问后台接口
 * @date 2026-11-10 10:00
 */
@Slf4j
@Component
public class UserTypeInterceptor implements HandlerInterceptor {

    /**
     * 后台接口前缀
     */
    private static final String ADMIN_PREFIX = "/admin";
    /**
     * 移动端接口前缀
     */
    private static final String MOBILE_PREFIX = "/mobile";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        User user = UserHolder.get();
        // 未登录(如登录/续期接口)或无类型信息时放行，交由前置拦截器处理
        if (Objects.isNull(user) || Objects.isNull(user.getType())) {
            return true;
        }
        // 拦截器匹配使用去掉 context-path(/api) 的路径
        Object pathAttr = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (!(pathAttr instanceof String path)) {
            return true;
        }
        Integer userType = user.getType();
        boolean illegal = (path.startsWith(ADMIN_PREFIX) && !UserTypeEnum.ADMIN.getType().equals(userType))
                || (path.startsWith(MOBILE_PREFIX) && !UserTypeEnum.MOBILE.getType().equals(userType));
        if (illegal) {
            log.warn("用户[{}]类型[{}]无权访问接口[{}]", user.getId(), userType, path);
            CommonErrorEnum.NO_AUTH_ERROR.sendHttpError();
            return false;
        }
        return true;
    }
}
