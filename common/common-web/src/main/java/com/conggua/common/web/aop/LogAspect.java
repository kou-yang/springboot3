package com.conggua.common.web.aop;

import com.conggua.common.base.util.NetworkUtils;
import com.conggua.common.base.util.SpringContextUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

/**
 * 请求响应日志 AOP
 *
 * @author kouyang
 **/
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final ObjectMapper objectMapper;

    /**
     * 执行拦截
     */
    @Around("execution(* com..controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 获取请求路径
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        String url = request.getRequestURI();
        // 获取请求参数
        String reqParam = buildRequestParam(request, point.getArgs());
        // 输出请求日志
        log.info("request start, path: {}, ip: {}, params: {}", url, NetworkUtils.getIpAddress(request), reqParam);
        // 执行原方法
        Object result = point.proceed();
        // 输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, cost: {}ms", totalTimeMillis);
        return result;
    }

    /**
     * 构建请求参数字符串，支持：
     * - GET: 拼接 query string（如 a=1&b=2）
     * - POST/PUT: 对象转 JSON，基本类型拼接，文件忽略
     */
    private String buildRequestParam(HttpServletRequest request, Object[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return "";
        }
        // 如果是 GET 请求，优先使用 request.getQueryString()
        if ("GET".equalsIgnoreCase(request.getMethod()) && StringUtils.isNotBlank(request.getQueryString())) {
            return request.getQueryString();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            Object arg = args[i];
            if (arg == null) {
                sb.append("null");
                continue;
            }
            // 跳过文件上传类型
            if (arg instanceof MultipartFile || (arg.getClass().isArray() && MultipartFile.class.isAssignableFrom(arg.getClass().getComponentType()))) {
                sb.append("<file>");
                continue;
            }
            // 转为 JSON
            try {
                String json = objectMapper.writeValueAsString(arg);
                // 如果是简单类型（如 String, Integer），writeValueAsString 会加引号，去掉外层引号
                if (json.startsWith("\"") && json.endsWith("\"") && !json.contains("\\\"")) {
                    json = json.substring(1, json.length() - 1);
                }
                sb.append(json);
            } catch (JsonProcessingException e) {
                // 转 JSON 失败，降级为 toString
                sb.append(arg);
            }
        }

        return sb.toString();
    }
}

