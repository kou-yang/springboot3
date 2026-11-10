package com.conggua.common.base.model.entity;

import lombok.Data;

/**
 * @author ky
 * @description
 * @date 2024-06-03 14:01
 */
@Data
public class User {

    private String id;

    private String deviceFingerprint;

    private String userName;

    /**
     * 用户类型：0-后台，1-移动端
     */
    private Integer type;
}
