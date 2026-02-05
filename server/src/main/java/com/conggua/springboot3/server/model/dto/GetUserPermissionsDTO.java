package com.conggua.springboot3.server.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author kouyang
 * @since 2026-02-02
 */
@Data
public class GetUserPermissionsDTO {

    @Schema(description = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @Schema(description = "资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限")
    private String type;
}