package com.conggua.springboot3.server.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@TableName("sys_log")
@Schema(name = "Log", description = "$!{table.comment}")
public class Log implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @TableId
    private String id;

    @Schema(description = "trace_id")
    @TableField("trace_id")
    private String traceId;

    @Schema(description = "日志级别")
    @TableField("level")
    private String level;

    @Schema(description = "时间")
    @TableField("time")
    private LocalDateTime time;

    @Schema(description = "日志内容")
    @TableField("content")
    private String content;
}