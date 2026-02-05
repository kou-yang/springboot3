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
import java.util.ArrayList;
import java.util.List;

/**
 * @author kouyang
 * @since 2026-02-05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_category")
@Schema(name = "Category", description = "$!{table.comment}")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId
    private String id;

    @Schema(description = "模块")
    @TableField("module")
    private Integer module;

    @Schema(description = "父级ID")
    @TableField("parent_id")
    private String parentId;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "备注")
    @TableField("note")
    private String note;

    @Schema(description = "创建人")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "子级")
    @TableField(exist = false)
    private List<Category> children = new ArrayList<>();
}