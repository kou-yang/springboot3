package com.conggua.common.web.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author ky
 * @description 主键id
 * @date 2024-04-22 17:33
 */
@Data
public class PrimaryKeyDTO {

    @Schema(description = "主键id")
    @NotBlank
    private String id;
}
