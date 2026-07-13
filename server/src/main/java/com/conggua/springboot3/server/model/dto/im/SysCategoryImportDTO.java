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
public class SysCategoryImportDTO {

    @ExcelProperty(value = "模块")
    private Integer module;

    @ExcelProperty(value = "父级ID")
    private String parentId;

    @ExcelProperty(value = "名称")
    private String name;

    @ExcelProperty(value = "备注")
    private String note;
}