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
 * @since 2026-02-02
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributeRoleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @NotBlank
    private String userId;

    @Schema(description = "角色id集合")
    @NotEmpty
    private List<String> roleIds;
}