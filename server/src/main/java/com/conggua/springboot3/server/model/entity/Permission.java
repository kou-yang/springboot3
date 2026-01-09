package com.conggua.springboot3.server.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author kouyang
 * @since 2026-01-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_permission")
@Schema(name = "Permission", description = "$!{table.comment}")
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @TableId
    private String id;

    @Schema(description = "父id")
    @TableField("parent_id")
    private String parentId;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "权限标识符")
    @TableField("code")
    private String code;

    @Schema(description = "资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限")
    @TableField("type")
    private String type;

    @Schema(description = "排序值")
    @TableField("sort")
    private Integer sort;

    @Schema(description = "图标")
    @TableField("icon")
    private String icon;

    @Schema(description = "URL路径")
    @TableField("url")
    private String url;

    @Schema(description = "HTTP方法")
    @TableField("method")
    private String method;

    @Schema(description = "扩展属性（JSON格式，按type约定结构）")
    @TableField("attrs")
    private String attrs;

    @Schema(description = "状态：0-禁用，1-启用")
    @TableField("status")
    private Byte status;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}