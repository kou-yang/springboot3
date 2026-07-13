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
public class SysRolePermissionImportDTO {

    @ExcelProperty(value = "资源类型：menu-菜单，button-按钮，api-接口，dataScope-数据权限")
    private String type;

    @ExcelProperty(value = "角色id")
    private String roleId;

    @ExcelProperty(value = "权限id")
    private String permissionId;
}