package com.conggua.springboot3.server.generator;

import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.generator.Generator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Properties;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-08-23 11:41
 */
public class Gen {

    /**
     * 模块名
     * commom/common-base
     * server
     */
    private static final String MODULE = "server";

    /**
     * 父包名
     */
    private static final String PARENT_PACKAGE = "com.conggua.springboot3.server";

    /**
     * 需要生成的表(默认不覆盖)
     */
    private static final List<String> TABLES = List.of("crm_company", "sys_role");

    public static void main(String[] args) {
        // 加载properties文件
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(new ClassPathResource("application-dev.yaml"));
        Properties properties = factory.getObject();
        Assert.notNull(properties, "配置文件不存在");
        // 获取数据库配置值
        String url = properties.getProperty("spring.datasource.url");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");
        if (StringUtils.isAnyBlank(url, username, password)) {
            throw new BusinessException(CommonErrorEnum.NOT_FOUND_ERROR, "缺少数据库配置");
        }
        // 生成模版代码
        Generator.create(url, username, password, TABLES, MODULE, PARENT_PACKAGE);
    }
}
