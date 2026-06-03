package com.conggua.springboot3.server.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.common.UserHolder;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.util.*;
import com.conggua.common.redis.util.RedisUtils;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.constant.LoginConstant;
import com.conggua.springboot3.server.constant.RedisKey;
import com.conggua.springboot3.server.designpattern.strategy.login.LoginStrategy;
import com.conggua.springboot3.server.designpattern.strategy.login.LoginStrategyContext;
import com.conggua.springboot3.server.mapper.UserMapper;
import com.conggua.springboot3.server.model.bo.UserInfo;
import com.conggua.springboot3.server.model.dto.UserLoginDTO;
import com.conggua.springboot3.server.model.dto.UserPageDTO;
import com.conggua.springboot3.server.model.dto.UserSaveDTO;
import com.conggua.springboot3.server.model.dto.UserUpdateDTO;
import com.conggua.springboot3.server.model.entity.Permission;
import com.conggua.springboot3.server.model.entity.RolePermission;
import com.conggua.springboot3.server.model.entity.User;
import com.conggua.springboot3.server.model.entity.UserRole;
import com.conggua.springboot3.server.model.vo.UserDetailVO;
import com.conggua.springboot3.server.model.vo.UserPageVO;
import com.conggua.springboot3.server.model.vo.UserPermissionVO;
import com.conggua.springboot3.server.service.PermissionService;
import com.conggua.springboot3.server.service.RolePermissionService;
import com.conggua.springboot3.server.service.UserRoleService;
import com.conggua.springboot3.server.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author kouyang
 * @since 2026-02-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(UserSaveDTO dto) {
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        passwordEncoder.encode(entity.getPassword());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        this.save(entity);

        entity.setPassword(null);
        return entity;
    }

    @Override
    public void remove(String id) {
        boolean one = this.removeById(id);
        CRUDUtil.validateDeleteSuccess(one);
    }

    @Override
    public User update(UserUpdateDTO dto) {
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        this.updateById(entity);
        return entity;
    }

    @Override
    public CommonPage<UserPageVO> page(UserPageDTO dto) {
        Page<User> page = dto.startMpPage(User.class);
        page = lambdaQuery()
                .like(StringUtils.isNotBlank(dto.getAccount()), User::getAccount, dto.getAccount())
                .like(StringUtils.isNotBlank(dto.getUserName()), User::getUserName, dto.getUserName())
                .like(StringUtils.isNotBlank(dto.getPhone()), User::getPhone, dto.getPhone())
                .page(page);
        // entity转vo
        List<UserPageVO> voList = this.entityList2PageVOList(page.getRecords());
        return CommonPage.restPage(voList, page.getTotal());
    }

    @Override
    public UserDetailVO getDetail(String id) {
        User entity = this.getById(id);
        return this.entity2DetailVO(entity);
    }

    @Override
    public Map<String, String> login(UserLoginDTO dto) {
        // 登录
        LoginStrategyContext loginStrategyContext = SpringContextUtils.getBean(LoginStrategyContext.class);
        LoginStrategy strategy = loginStrategyContext.getStrategy(dto.getType());
        return strategy.execute(dto);
    }

    @Override
    public String renewal(String refreshToken) {
        DecodedJWT jwt = JwtUtils.verify(refreshToken, false);
        if (Objects.isNull(jwt)) {
            throw new BusinessException(CommonErrorEnum.NOT_LOGIN_ERROR);
        }
        Map<String, ?> map = JwtUtils.getMap(jwt);
        String userId = (String) map.get("userId");
        String deviceFingerprint = (String) map.get("deviceFingerprint");
        if (StringUtils.isAnyBlank(userId, deviceFingerprint)) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "RefreshToken 错误");
        }
        String latestRefreshToken = RedisUtils.get(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId, deviceFingerprint), String.class);
        if (!Objects.equals(refreshToken, latestRefreshToken)) {
            throw new BusinessException(CommonErrorEnum.NOT_LOGIN_ERROR);
        }
        // 生成新的 AccessToken
        Map<String, String> m = Map.of("userId", userId, "deviceFingerprint", deviceFingerprint);
        String accessToken = JwtUtils.generateToken(m, true);
        refreshToken = JwtUtils.generateToken(m, false);
        // 存储到 redis
        RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId, deviceFingerprint), accessToken, LoginConstant.ACCESS_TOKEN_EXPIRE, TimeUnit.MINUTES);
        RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId, deviceFingerprint), refreshToken, LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);
        RedisUtils.expire(RedisKey.getKeyNoTenant(RedisKey.USER_INFO, userId), LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);

        // 存储 RefreshToken 到 Cookie
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/api/user/renewal");
        refreshCookie.setMaxAge((int) (LoginConstant.REFRESH_TOKEN_EXPIRE * 24 * 3600));
        HttpServletResponse response = SpringContextUtils.getHttpServletResponse();
        response.addHeader("Set-Cookie",
                String.format("refreshToken=%s; HttpOnly; Secure; SameSite=Strict; Path=/api/user/renewal; Max-Age=%d",
                        refreshToken, LoginConstant.REFRESH_TOKEN_EXPIRE * 24 * 3600));
        return accessToken;
    }

    @Override
    public void logout() {
        UserInfo user = (UserInfo) UserHolder.get();
        String userId = user.getId();
        String deviceFingerprint = user.getDeviceFingerprint();
        RedisUtils.delete(RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId, deviceFingerprint));
        RedisUtils.delete(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId, deviceFingerprint));
    }

    @Override
    public List<UserPermissionVO> listUserPermissions(String userId, String type) {
        // 获取用户的角色ID列表
        List<UserRole> userRoleList = userRoleService.listBy(UserRole::getUserId, userId);
        if (CollectionUtils.isEmpty(userRoleList)) {
            // 用户没有角色，则没有权限
            return List.of();
        }

        // 提取角色ID
        List<String> roleIds = CollStreamUtils.toList(userRoleList, UserRole::getRoleId);
        // 根据角色ID和权限类型查询角色权限关联记录
        List<RolePermission> rolePermissionList = rolePermissionService.lambdaQuery()
                .in(RolePermission::getRoleId, roleIds)
                .eq(StringUtils.isNotBlank(type), RolePermission::getType, type)
                .list();
        if (rolePermissionList.isEmpty()) {
            // 没有权限记录
            return List.of();
        }
        
        // 提取权限ID
        List<String> permissionIds = CollStreamUtils.toList(rolePermissionList, RolePermission::getPermissionId);
        // 查询权限详情
        List<Permission> permissionList = permissionService.lambdaQuery()
                .in(Permission::getId, permissionIds)
                .eq(StringUtils.isNotBlank(type), Permission::getType, type)
                .eq(Permission::getStatus, true)
                .orderByAsc(Permission::getSort)
                .list();

        List<UserPermissionVO> voList = CollStreamUtils.toList(permissionList, permission -> {
            UserPermissionVO vo = new UserPermissionVO();
            BeanUtils.copyProperties(permission, vo);
            return vo;
        });
        return TreeUtils.translate(voList, UserPermissionVO::getId, UserPermissionVO::getParentId);
    }

    @Override
    public UserDetailVO getUserInfo() {
        User user = getById(UserHolder.getUserId());
        return entity2DetailVO(user);
    }

    public List<UserPageVO> entityList2PageVOList(List<User> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return CollStreamUtils.toList(entityList, entity -> {
            UserPageVO vo = new UserPageVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
    }

    public UserDetailVO entity2DetailVO(User entity) {
        if (entity == null) {
            return null;
        }
        UserDetailVO vo = new UserDetailVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}