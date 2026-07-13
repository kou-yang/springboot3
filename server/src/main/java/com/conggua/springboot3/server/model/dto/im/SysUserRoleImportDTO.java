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
public class SysUserRoleImportDTO {

    @ExcelProperty(value = "用户id")
    private String userId;

    @ExcelProperty(value = "角色id")
    private String roleId;
}