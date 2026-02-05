package com.conggua.springboot3.server.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author kouyang
 * @since 2026-02-02
 */
@Data
public class UserRoleDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private String id;

    @Schema(description = "用户id")
    private String userId;

    @Schema(description = "角色id")
    private String roleId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}