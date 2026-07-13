package com.conggua.springboot3.server.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author kouyang
 * @since 2026-07-10
 */
@Data
public class LogDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id;

    @Schema(description = "trace_id")
    private String traceId;

    @Schema(description = "日志级别")
    private String level;

    @Schema(description = "时间")
    private LocalDateTime time;

    @Schema(description = "日志内容")
    private String content;
}