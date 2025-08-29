package com.conggua.common.web.model.vo;

import lombok.Data;

/**
 * @author ky
 * @description
 * @date 2024-07-30 14:10
 */
@Data
public class PresignedUrlVo {

    private String key;

    private String presignedUrl;
}
