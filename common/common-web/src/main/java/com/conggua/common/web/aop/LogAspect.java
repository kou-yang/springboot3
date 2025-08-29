package com.conggua.common.web.aop;

import com.conggua.common.base.util.NetworkUtils;
import com.conggua.common.base.util.SpringContextUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.UUID;

/**
 * 请求响应日志 AOP
 *
 * @author kouyang
 **/
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 执行拦截
     */
    @Around("execution(* com..controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 获取请求路径
        HttpServletRequest httpServletRequest = SpringContextUtils.getHttpServletRequest();
        // 生成请求唯一 id
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";
        // 输出请求日志
        log.info("request start, id: {}, path: {}, ip: {}, params: {}", requestId, url, NetworkUtils.getIpAddress(httpServletRequest), reqParam);
        // 执行原方法
        Object result = point.proceed();
        // 输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, cost: {}ms", requestId, totalTimeMillis);
        return result;
    }
}

