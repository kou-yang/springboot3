package com.conggua.springboot3.server.controller;

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.base.common.UserHolder;
import com.conggua.common.web.model.request.PrimaryKeyDTO;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.model.dto.*;
import com.conggua.springboot3.server.model.entity.User;
import com.conggua.springboot3.server.model.vo.UserDetailVO;
import com.conggua.springboot3.server.model.vo.UserPageVO;
import com.conggua.springboot3.server.model.vo.UserPermissionVO;
import com.conggua.springboot3.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author kouyang
 * @since 2026-02-02
 */
@Tag(name = "sys-用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "保存")
    @PostMapping("/save")
    public Result<?> save(@Validated @RequestBody UserSaveDTO dto) {
        User entity = userService.save(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody PrimaryKeyDTO dto) {
        userService.remove(dto.id());
        return ResultUtils.success();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<?> update(@Validated @RequestBody UserUpdateDTO dto) {
        User entity = userService.update(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public Result<CommonPage<UserPageVO>> page(@Validated @RequestBody UserPageDTO dto) {
        CommonPage<UserPageVO> page = userService.page(dto);
        return ResultUtils.success(page);
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("/detail/{id}")
    public Result<UserDetailVO> getDetail(@PathVariable String id) {
        UserDetailVO vo = userService.getDetail(id);
        return ResultUtils.success(vo);
    }

//    @Operation(summary = "查询全部")
//    @GetMapping("/all")
//    public Result<List<User>> listAll() {
//        List<User> list = userService.list();
//        return ResultUtils.success(list);
//    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<?> login(@Validated @RequestBody UserLoginDTO dto) {
        Map<String, String> tokenMap = userService.login(dto);
        return ResultUtils.success(tokenMap);
    }

    @Operation(summary = "token续期")
    @PostMapping("/renewal")
    public Result<?> renewal(@Validated @RequestBody TokenRenewalDTO dto) {
        String accessToken = userService.renewal(dto.getRefreshToken());
        return ResultUtils.success(accessToken);
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<?> logout() {
        userService.logout();
        return ResultUtils.success();
    }

    @Operation(summary = "获取当前用户权限")
    @GetMapping("/permissions")
    public Result<List<UserPermissionVO>> listUserPermissions(String type) {
        List<UserPermissionVO> permissions = userService.listUserPermissions(UserHolder.getUserId(), type);
        return ResultUtils.success(permissions);
    }
}
