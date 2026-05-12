package com.conggua.common.web.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * @author ky
 * @description 主键id
 * @date 2024-04-22 17:33
 */
public record PrimaryKeysDTO(

    @Schema(description = "主键id")
    @NotEmpty
    List<String> ids
) {}
