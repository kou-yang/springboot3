package com.conggua.springboot3.server.controller;

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.redis.annotation.Idempotent;
import com.conggua.common.web.model.request.PrimaryKeyDTO;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.model.dto.DistributePermissionDTO;
import com.conggua.springboot3.server.model.dto.RolePermissionPageDTO;
import com.conggua.springboot3.server.model.dto.RolePermissionSaveDTO;
import com.conggua.springboot3.server.model.entity.RolePermission;
import com.conggua.springboot3.server.model.vo.RolePermissionPageVO;
import com.conggua.springboot3.server.service.RolePermissionService;
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
 * @since 2026-01-09
 */
@Tag(name = "角色权限管理")
@RestController
@RequestMapping("/rolePermission")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @Operation(summary = "保存")
    @Idempotent(key = "#dto.roleId + #dto.permissionId")
    @PostMapping("/save")
    public Result<?> save(@Validated @RequestBody RolePermissionSaveDTO dto) {
        RolePermission entity = rolePermissionService.save(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody PrimaryKeyDTO dto) {
        rolePermissionService.remove(dto.id());
        return ResultUtils.success();
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public Result<CommonPage<RolePermissionPageVO>> page(@Validated @RequestBody RolePermissionPageDTO dto) {
        CommonPage<RolePermissionPageVO> page = rolePermissionService.page(dto);
        return ResultUtils.success(page);
    }

    @Operation(summary = "分配权限")
    @PostMapping("/distribute")
    public Result<?> distribute(@Validated @RequestBody DistributePermissionDTO dto) {
        rolePermissionService.distribute(dto);
        return ResultUtils.success();
    }
}
