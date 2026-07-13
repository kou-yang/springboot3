package com.conggua.springboot3.server.model.dto.im;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.fesod.sheet.annotation.ExcelProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author kouyang
 * @since 2026-05-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysDepartImportDTO {

    @ExcelProperty(value = "租户ID")
    private String tenantId;

    @ExcelProperty(value = "父id")
    private String parentId;

    @ExcelProperty(value = "部门名称")
    private String name;

    @ExcelProperty(value = "部门负责人用户id")
    private String header;

    @ExcelProperty(value = "jiage")
    private BigDecimal price;

    @ExcelProperty(value = "shijian")
    private LocalDateTime uTime;

    @ExcelProperty(value = "备注")
    private String note;
}