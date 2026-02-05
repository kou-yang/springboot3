package com.conggua.springboot3.server.controller;

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.web.model.request.PrimaryKeyDTO;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.model.dto.DistributeRoleDTO;
import com.conggua.springboot3.server.model.dto.UserRolePageDTO;
import com.conggua.springboot3.server.model.dto.UserRoleSaveDTO;
import com.conggua.springboot3.server.model.dto.UserRoleUpdateDTO;
import com.conggua.springboot3.server.model.entity.UserRole;
import com.conggua.springboot3.server.model.vo.UserRoleDetailVO;
import com.conggua.springboot3.server.model.vo.UserRolePageVO;
import com.conggua.springboot3.server.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kouyang
 * @since 2026-02-02
 */
@Tag(name = "sys-用户角色管理")
@RestController
@RequestMapping("/userRole")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @Operation(summary = "保存")
    @PostMapping("/save")
    public Result<?> save(@Validated @RequestBody UserRoleSaveDTO dto) {
        UserRole entity = userRoleService.save(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody PrimaryKeyDTO dto) {
        userRoleService.remove(dto.id());
        return ResultUtils.success();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<?> update(@Validated @RequestBody UserRoleUpdateDTO dto) {
        UserRole entity = userRoleService.update(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public Result<CommonPage<UserRolePageVO>> page(@Validated @RequestBody UserRolePageDTO dto) {
        CommonPage<UserRolePageVO> page = userRoleService.page(dto);
        return ResultUtils.success(page);
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("/detail/{id}")
    public Result<UserRoleDetailVO> getDetail(@PathVariable String id) {
        UserRoleDetailVO vo = userRoleService.getDetail(id);
        return ResultUtils.success(vo);
    }

    @Operation(summary = "查询全部")
    @GetMapping("/all")
    public Result<List<UserRole>> listAll() {
        List<UserRole> list = userRoleService.list();
        return ResultUtils.success(list);
    }

    @Operation(summary = "分配角色")
    @PostMapping("/distribute")
    public Result<?> distribute(@Validated @RequestBody DistributeRoleDTO dto) {
        userRoleService.distribute(dto);
        return ResultUtils.success();
    }

    @Operation(summary = "查询用户角色")
    @GetMapping("/roles")
    public Result<List<UserRole>> listUserRoles(String userId) {
        List<UserRole> list = userRoleService.listBy(UserRole::getUserId, userId);
        return ResultUtils.success(list);
    }
}
