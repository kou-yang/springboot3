package com.conggua.springboot3.server.config;

import com.conggua.springboot3.server.interceptor.CollectorInterceptor;
import com.conggua.springboot3.server.interceptor.TokenInterceptor;
import com.conggua.springboot3.server.interceptor.UserTypeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
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

    /**
     * 是否开启接口文档(springdoc)
     */
    @Value("${springdoc.api-docs.enabled:false}")
    private boolean apiDocsEnabled;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = new ArrayList<>(List.of(
                "/admin/user/login",
                "/admin/user/renewal"
        ));
        // 放行 knife4j 接口文档路径
        if (apiDocsEnabled) {
            excludePath.addAll(List.of(
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/webjars/**",
                    "/swagger-resources",
                    "/swagger-resources/**",
                    "/doc.html",
                    "/favicon.ico",
                    "/csrf"
            ));
        }

        registry.addInterceptor(tokenInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        registry.addInterceptor(collectorInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        registry.addInterceptor(userTypeInterceptor)
                .order(3)
                .addPathPatterns("/admin/**", "/mobile/**");
    }
}
