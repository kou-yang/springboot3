package com.conggua.springboot3.server.model.dto.im;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.fesod.sheet.annotation.ExcelProperty;

import java.time.LocalDateTime;

/**
 * @author kouyang
 * @since 2026-07-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogImportDTO {

    @ExcelProperty(value = "trace_id")
    private String traceId;

    @ExcelProperty(value = "日志级别")
    private String level;

    @ExcelProperty(value = "时间")
    private LocalDateTime time;

    @ExcelProperty(value = "日志内容")
    private String content;
}