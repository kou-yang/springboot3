package com.conggua.common.oss.minio.autoconfigure;

import com.conggua.common.oss.minio.core.MinIOTemplate;
import io.minio.MinioClient;
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
 * @date 2023-12-15 10:35
 */
@AutoConfiguration
@EnableConfigurationProperties(MinIOProperties.class)
@ConditionalOnProperty(prefix = "object.storage.minio", name = "enabled", havingValue = "true")
public class MinIOConfiguration {

    @Bean
    @SneakyThrows
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient minioClient(MinIOProperties ossProperties) {
        return MinioClient.builder()
                .endpoint(ossProperties.getEndpoint())
                .credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
                .build();
    }

    @Bean
    @ConditionalOnBean({MinioClient.class})
    @ConditionalOnMissingBean(MinIOTemplate.class)
    public MinIOTemplate minioTemplate(MinioClient minioClient, MinIOProperties ossProperties) {
        return new MinIOTemplate(minioClient, ossProperties);
    }
}