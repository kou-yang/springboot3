package com.conggua.common.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

/**
 * Knife4j 接口文档配置
 * 访问：http://localhost:8080/api/doc.html
 *
 * @author kouyang
 */
public class BaseKnife4jConfig {

    @Bean
    public OpenAPI defaultApi3() {
        return new OpenAPI()
                .info(new Info()
                        .title("template-3")
                        .description("template-3")
                        .contact(new Contact().name("KouYang").email(""))
                        .version("1.0")
                        .license(new License().name("Apache 2.0")));
    }
}