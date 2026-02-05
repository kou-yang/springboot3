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
 * @since 2026-02-05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @Schema(description = "模块")
    private Integer module;

    @Schema(description = "父级ID")
    private String parentId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String note;
}