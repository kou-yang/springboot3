package com.conggua.springboot3.server.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author kouyang
 * @since 2026-01-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionSaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色id")
    @NotBlank
    private String roleId;

    @Schema(description = "权限id")
    @NotBlank
    private String permissionId;
}