package com.conggua.springboot3.server.model.dto;

import com.conggua.common.web.model.request.CommonPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author kouyang
 * @since 2026-01-09
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PermissionPageDTO extends CommonPageDTO {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "权限标识符")
    private String code;
}