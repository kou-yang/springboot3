package com.conggua.springboot3.server.controller;

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.redis.annotation.Idempotent;
import com.conggua.common.web.model.request.PrimaryKeyDTO;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.model.dto.RolePageDTO;
import com.conggua.springboot3.server.model.dto.RoleSaveDTO;
import com.conggua.springboot3.server.model.dto.RoleUpdateDTO;
import com.conggua.springboot3.server.model.entity.Role;
import com.conggua.springboot3.server.model.vo.RoleDetailVO;
import com.conggua.springboot3.server.model.vo.RolePageVO;
import com.conggua.springboot3.server.service.RoleService;
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
@Tag(name = "sys-角色管理")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "保存")
    @Idempotent(key = "#dto.code")
    @PostMapping("/save")
    public Result<?> save(@Validated @RequestBody RoleSaveDTO dto) {
        Role entity = roleService.save(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody PrimaryKeyDTO dto) {
        roleService.remove(dto.id());
        return ResultUtils.success();
    }

    @Operation(summary = "更新")
    @Idempotent(key = "#dto.code ?: 'default_code'")
    @PostMapping("/update")
    public Result<?> update(@Validated @RequestBody RoleUpdateDTO dto) {
        Role entity = roleService.update(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public Result<CommonPage<RolePageVO>> page(@Validated @RequestBody RolePageDTO dto) {
        CommonPage<RolePageVO> page = roleService.page(dto);
        return ResultUtils.success(page);
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("/detail/{id}")
    public Result<RoleDetailVO> getDetail(@PathVariable String id) {
        RoleDetailVO vo = roleService.getDetail(id);
        return ResultUtils.success(vo);
    }

    @Operation(summary = "查询全部")
    @GetMapping("/all")
    public Result<List<Role>> listAll() {
        List<Role> list = roleService.list();
        return ResultUtils.success(list);
    }
}
