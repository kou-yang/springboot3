package com.conggua.common.oss.cos.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ky
 * @description
 * @date 2024-07-05 11:48
 */
@Data
@ConfigurationProperties(prefix = "object.storage.cos")
public class COSProperties {

    /**
     * 是否开启
     */
    private Boolean enabled = false;

    /**
     * bucket 地域
     */
    private String region;

    /**
     * secretId
     */
    private String secretId;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * APPID
     */
    private String appid;

    /**
     * url过期时间，单位：毫秒（ms），默认30m
     */
    private Long presignedUrlExpiration = 30 * 60 * 1000L;

    /**
     * 分块上传的块大小，单位：字节（Byte），默认5MB
     */
    private Long minimumUploadPartSize = 5 * 1024 * 1024L;

    /**
     * 大于等于该值则并发的分块上传文件，单位：字节（Byte），默认5MB
     */
    private Long multipartUploadThreshold = 5 * 1024 * 1024L;
}
