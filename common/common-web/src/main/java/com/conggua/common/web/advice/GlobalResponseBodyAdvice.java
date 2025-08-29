package com.conggua.common.web.advice;

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author ky
 * @description
 * @date 2024-04-20 22:03
 */
@Slf4j
@RestControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${global-response-switch.enabled:false}")
    private Boolean enabled;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果当前请求是 swagger 相关请求则不统一返回结果
        return enabled && !returnType.getDeclaringClass().isAssignableFrom(BasicErrorController.class);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 如果当前请求是 swagger 相关请求则不统一返回结果
        if (request.getURI().getPath().startsWith(contextPath + "/swagger") || request.getURI().getPath().startsWith(contextPath + "/v2/api-docs")) {
            return body;
        }
        if (body instanceof String) {
            return objectMapper.writeValueAsString(ResultUtils.success(body));
        }
        if (body instanceof Result<?>) {
            return body;
        }
        return ResultUtils.success(body);
    }
}
