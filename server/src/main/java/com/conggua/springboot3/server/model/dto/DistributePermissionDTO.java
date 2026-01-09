package com.conggua.springboot3.server.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author kouyang
 * @since 2026-01-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributePermissionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限")
    @NotBlank
    private String type;

    @Schema(description = "角色id")
    @NotBlank
    private String roleId;

    @Schema(description = "权限id集合")
    @NotEmpty
    private List<String> permissionIds;
}