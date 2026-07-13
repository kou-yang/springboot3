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
public class SysRoleImportDTO {

    @ExcelProperty(value = "名称")
    private String name;

    @ExcelProperty(value = "编码")
    private String code;

    @ExcelProperty(value = "备注")
    private String remark;
}