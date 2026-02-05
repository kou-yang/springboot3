package com.conggua.springboot3.server.controller;

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.redis.annotation.Idempotent;
import com.conggua.common.web.model.request.PrimaryKeyDTO;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.model.dto.PermissionPageDTO;
import com.conggua.springboot3.server.model.dto.PermissionSaveDTO;
import com.conggua.springboot3.server.model.dto.PermissionUpdateDTO;
import com.conggua.springboot3.server.model.entity.Permission;
import com.conggua.springboot3.server.model.vo.PermissionDetailVO;
import com.conggua.springboot3.server.model.vo.PermissionPageVO;
import com.conggua.springboot3.server.model.vo.UserPermissionVO;
import com.conggua.springboot3.server.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kouyang
 * @since 2026-01-09
 */
@Tag(name = "sys-权限管理")
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "保存")
    @Idempotent(key = "#dto.type + #dto.code")
    @PostMapping("/save")
    public Result<?> save(@Validated @RequestBody PermissionSaveDTO dto) {
        Permission entity = permissionService.save(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody PrimaryKeyDTO dto) {
        permissionService.remove(dto.id());
        return ResultUtils.success();
    }

    @Operation(summary = "更新")
    @Idempotent(key = "#dto.code ?: 'default_code'")
    @PostMapping("/update")
    public Result<?> update(@Validated @RequestBody PermissionUpdateDTO dto) {
        Permission entity = permissionService.update(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public Result<CommonPage<PermissionPageVO>> page(@Validated @RequestBody PermissionPageDTO dto) {
        CommonPage<PermissionPageVO> page = permissionService.page(dto);
        return ResultUtils.success(page);
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("/detail/{id}")
    public Result<PermissionDetailVO> getDetail(@PathVariable String id) {
        PermissionDetailVO vo = permissionService.getDetail(id);
        return ResultUtils.success(vo);
    }

    @Operation(summary = "查询全部")
    @GetMapping("/all")
    public Result<List<UserPermissionVO>> listAll(String type, Boolean status) {
        List<UserPermissionVO> tree = permissionService.all(type, status);
        return ResultUtils.success(tree);
    }
}
