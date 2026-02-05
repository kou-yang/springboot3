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
 * @since 2026-02-02
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
@Schema(name = "User", description = "$!{table.comment}")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @TableId
    private String id;

    @Schema(description = "账号")
    @TableField("account")
    private String account;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "盐")
    @TableField("salt")
    private String salt;

    @Schema(description = "用户名")
    @TableField("user_name")
    private String userName;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "性别")
    @TableField("gender")
    private Byte gender;

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}