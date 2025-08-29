package com.conggua.common.oss.minio.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ky
 * @description
 * @date 2023-12-15 10:05
 */
@Data
@ConfigurationProperties(prefix = "object.storage.minio")
public class MinIOProperties {

    /**
     * 是否开启
     */
    private Boolean enabled = false;

    /**
     * 访问域名
     */
    private String endpoint;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 存储空间
     */
    private String bucketName;
}
