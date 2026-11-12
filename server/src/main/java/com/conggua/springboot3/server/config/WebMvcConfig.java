package com.conggua.springboot3.server.config;

import com.conggua.springboot3.server.interceptor.CollectorInterceptor;
import com.conggua.springboot3.server.interceptor.TokenInterceptor;
import com.conggua.springboot3.server.interceptor.UserTypeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author ky
 * @description
 * @date 2024-05-15 15:57
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Autowired
    private CollectorInterceptor collectorInterceptor;
    @Autowired
    private UserTypeInterceptor userTypeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 免登录放行路径
        List<String> excludePath = List.of(
                "/admin/user/login",
                "/admin/user/renewal",
                // 健康检查（运维探活）
                "/actuator/health",
                // swagger
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/doc.html",
                "/favicon.ico",
                "/csrf"
        );

        registry.addInterceptor(tokenInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        registry.addInterceptor(collectorInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        // 端类型校验：仅区分 /admin 与 /mobile
        registry.addInterceptor(userTypeInterceptor)
                .order(3)
                .addPathPatterns("/admin/**", "/mobile/**");
    }
}
