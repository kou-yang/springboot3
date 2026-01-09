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
@TableName("sys_role_permission")
@Schema(name = "RolePermission", description = "$!{table.comment}")
public class RolePermission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @TableId
    private String id;

    @Schema(description = "资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限")
    @TableField("type")
    private String type;

    @Schema(description = "角色id")
    @TableField("role_id")
    private String roleId;

    @Schema(description = "权限id")
    @TableField("permission_id")
    private String permissionId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}