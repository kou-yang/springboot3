package com.conggua.springboot3.server.model.dto.im;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.fesod.sheet.annotation.ExcelProperty;

/**
 * @author kouyang
 * @since 2026-05-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysPermissionImportDTO {

    @ExcelProperty(value = "父id")
    private String parentId;

    @ExcelProperty(value = "名称")
    private String name;

    @ExcelProperty(value = "权限标识符")
    private String code;

    @ExcelProperty(value = "资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限")
    private String type;

    @ExcelProperty(value = "排序值")
    private Integer sort;

    @ExcelProperty(value = "图标")
    private String icon;

    @ExcelProperty(value = "URL路径")
    private String url;

    @ExcelProperty(value = "HTTP方法")
    private String method;

    @ExcelProperty(value = "扩展属性（JSON格式，按type约定结构）")
    private String attrs;

    @ExcelProperty(value = "状态：0-禁用，1-启用")
    private Byte status;

    @ExcelProperty(value = "备注")
    private String remark;
}