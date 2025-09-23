package com.conggua.common.web.config;

import com.conggua.common.web.filter.MDCFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-09-19 15:38
 */
@Configuration
public class MDCFilterConfig {

    @Bean
    public FilterRegistrationBean<MDCFilter> traceIdFilter() {
        FilterRegistrationBean<MDCFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MDCFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}