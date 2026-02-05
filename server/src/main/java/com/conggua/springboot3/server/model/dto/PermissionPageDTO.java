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

    @Schema(description = "资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限")
    private String type;

    @Schema(description = "状态：0-禁用，1-启用")
    private Boolean status;
}