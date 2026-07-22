package com.conggua.springboot3.server.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author kouyang
 * @since 2026-07-22
 */
@Data
public class LogCursorVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "日志列表")
    private List<LogPageVO> logs;

    @Schema(description = "下一页游标")
    private String nextCursor;

    @Schema(description = "是否有更多数据")
    private Boolean hasMore;
}