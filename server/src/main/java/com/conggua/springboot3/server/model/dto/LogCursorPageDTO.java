package com.conggua.springboot3.server.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author kouyang
 * @since 2026-07-10
 */
@Data
@ToString(callSuper = true)
public class LogCursorPageDTO {

    @Schema(description = "链路id")
    private String traceId;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "日志内容")
    private String content;

    @Schema(description = "游标")
    private String cursor;
}