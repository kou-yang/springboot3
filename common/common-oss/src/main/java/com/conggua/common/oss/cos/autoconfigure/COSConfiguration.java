package com.conggua.common.oss.cos.autoconfigure;

import com.conggua.common.oss.cos.core.COSTemplate;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author ky
 * @description
 * @date 2024-07-05 11:58
 */
@AutoConfiguration
@EnableConfigurationProperties(COSProperties.class)
@ConditionalOnProperty(prefix = "object.storage.cos", name = "enabled", havingValue = "true")
public class COSConfiguration {

    @Bean
    @SneakyThrows
    @ConditionalOnMissingBean(COSClient.class)
    public COSClient cosClient(COSProperties cosProperties) {
        COSCredentials cred = new BasicCOSCredentials(cosProperties.getSecretId(), cosProperties.getSecretKey());
        Region region = new Region(cosProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cred, clientConfig);
    }

    @Bean
    @ConditionalOnBean({COSClient.class})
    @ConditionalOnMissingBean(COSTemplate.class)
    public COSTemplate cosTemplate(COSClient cosClient, COSProperties cosProperties) {
        return new COSTemplate(cosClient, cosProperties);
    }
}