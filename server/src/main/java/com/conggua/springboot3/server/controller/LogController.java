package com.conggua.springboot3.server.controller;

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.web.model.request.PrimaryKeyDTO;
import com.conggua.common.web.model.request.PrimaryKeysDTO;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.model.dto.LogPageDTO;
import com.conggua.springboot3.server.model.vo.LogPageVO;
import com.conggua.springboot3.server.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kouyang
 * @since 2026-07-10
 */
@Tag(name = "日志管理")
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody PrimaryKeyDTO dto) {
        logService.remove(dto.id());
        return ResultUtils.success();
    }

    @Operation(summary = "批量删除")
    @PostMapping("/batch/delete")
    public Result<?> deleteBatch(@Validated @RequestBody PrimaryKeysDTO dto) {
        dto.ids().forEach(logService::remove);
        return ResultUtils.success();
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public Result<CommonPage<LogPageVO>> page(@Validated @RequestBody LogPageDTO dto) {
        CommonPage<LogPageVO> page = logService.page(dto);
        return ResultUtils.success(page);
    }
}
