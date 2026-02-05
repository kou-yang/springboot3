package com.conggua.springboot3.server.config;

import com.conggua.springboot3.server.intecepter.CollectorInterceptor;
import com.conggua.springboot3.server.intecepter.TokenInterceptor;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = List.of(
                "/user/login",
                "/user/renewal",
                // 放行 knife4j 接口文档路径
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
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        registry.addInterceptor(collectorInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
    }
}
