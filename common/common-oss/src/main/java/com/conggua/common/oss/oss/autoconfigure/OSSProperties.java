package com.conggua.common.oss.oss.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ky
 * @description
 * @date 2024-07-01 11:10
 */
@Data
@ConfigurationProperties(prefix = "object.storage.oss")
public class OSSProperties {

    /**
     * 是否开启
     */
    private Boolean enabled = false;

    /**
     * 访问域名
     */
    private String endpoint;

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * 存储空间
     */
    private String bucketName;

    /**
     * url过期时间，单位：毫秒（ms），默认30m
     */
    private Long presignedUrlExpiration = 30 * 60 * 1000L;
}
