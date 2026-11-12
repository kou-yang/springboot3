package com.conggua.springboot3.server.controller.admin;

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.base.common.UserHolder;
import com.conggua.common.redis.annotation.RateLimiter;
import com.conggua.common.web.annotation.PreAuth;
import com.conggua.common.web.model.request.PrimaryKeyDTO;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.model.dto.UserLoginDTO;
import com.conggua.springboot3.server.model.dto.UserPageDTO;
import com.conggua.springboot3.server.model.dto.UserSaveDTO;
import com.conggua.springboot3.server.model.dto.UserUpdateDTO;
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
@Tag(name = "[后台] sys-用户管理")
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "保存")
    @PreAuth("hasPermission('user:save')")
    @PostMapping("/save")
    public Result<?> save(@Validated @RequestBody UserSaveDTO dto) {
        User entity = userService.save(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "删除")
    @PreAuth("hasPermission('user:delete')")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody PrimaryKeyDTO dto) {
        userService.remove(dto.id());
        return ResultUtils.success();
    }

    @Operation(summary = "更新")
    @PreAuth("hasPermission('user:update')")
    @PostMapping("/update")
    public Result<?> update(@Validated @RequestBody UserUpdateDTO dto) {
        User entity = userService.update(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "分页查询")
    @PreAuth("hasPermission('user:page')")
    @PostMapping("/page")
    public Result<CommonPage<UserPageVO>> page(@Validated @RequestBody UserPageDTO dto) {
        CommonPage<UserPageVO> page = userService.page(dto);
        return ResultUtils.success(page);
    }

    @Operation(summary = "根据id查询详情")
    @PreAuth("hasPermission('user:detail')")
    @GetMapping("/detail/{id}")
    public Result<UserDetailVO> getDetail(@PathVariable String id) {
        UserDetailVO vo = userService.getDetail(id);
        return ResultUtils.success(vo);
    }

    @Operation(summary = "登录")
    @RateLimiter(time = 60L, count = 10)
    @PostMapping("/login")
    public Result<?> login(@Validated @RequestBody UserLoginDTO dto) {
        Map<String, String> tokenMap = userService.login(dto);
        return ResultUtils.success(tokenMap);
    }

    @Operation(summary = "token续期")
    @PostMapping("/renewal")
    public Result<?> renewal(String refreshToken) {
        Map<String, String> tokenPair = userService.renewal(refreshToken);
        return ResultUtils.success(tokenPair);
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<?> logout() {
        userService.logout();
        return ResultUtils.success();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<UserDetailVO> getUserInfo() {
        UserDetailVO info = userService.getUserInfo();
        return ResultUtils.success(info);
    }

    @Operation(summary = "获取当前用户权限")
    @GetMapping("/permissions")
    public Result<List<UserPermissionVO>> listUserPermissions(String type) {
        List<UserPermissionVO> permissions = userService.listUserPermissions(UserHolder.getUserId(), type);
        return ResultUtils.success(permissions);
    }
}
