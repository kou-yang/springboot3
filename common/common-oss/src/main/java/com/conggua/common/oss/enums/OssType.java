package com.conggua.common.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OssType {

    /**
     * Minio
     */
    MINIO("minio", 1),

    /**
     * 华为 OBS
     */
    OBS("obs", 2),

    /**
     * 腾讯 COS
     */
    COS("tencent", 3),

    /**
     * 阿里云 OSS
     */
    OSS("alibaba", 4)
    ;

    /**
     * 名称
     */
    public final String name;

    /**
     * 类型
     */
    public final int type;
}
