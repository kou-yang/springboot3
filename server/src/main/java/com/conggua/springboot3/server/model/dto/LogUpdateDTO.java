package com.conggua.springboot3.server.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author kouyang
 * @since 2026-07-10
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
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