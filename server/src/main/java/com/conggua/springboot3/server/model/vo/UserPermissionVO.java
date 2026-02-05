package com.conggua.springboot3.server.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kouyang
 * @since 2026-02-02
 */
@Data
public class UserPermissionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "权限id")
    private String id;

    @Schema(description = "父id")
    private String parentId;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "权限标识符")
    private String code;

    @Schema(description = "资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限")
    private String type;

    @Schema(description = "排序值")
    private Integer sort;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "URL路径")
    private String url;

    @Schema(description = "HTTP方法")
    private String method;

    @Schema(description = "扩展属性（JSON格式，按type约定结构）")
    private String attrs;

    @Schema(description = "子")
    private List<UserPermissionVO> children = new ArrayList<>();
}