package com.conggua.springboot3.server.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.common.UserHolder;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.util.CRUDUtil;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.base.util.JwtUtils;
import com.conggua.common.base.util.TreeUtils;
import com.conggua.common.redis.util.RedisUtils;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.constant.LoginConstant;
import com.conggua.springboot3.server.constant.RedisKey;
import com.conggua.springboot3.server.constant.UserTypeEnum;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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

    /**
     * 策略上下文依赖 UserService，存在循环依赖，用 @Lazy 代理打破
     */
    @Autowired
    @Lazy
    private LoginStrategyContext loginStrategyContext;

    @Override
    public User save(UserSaveDTO dto) {
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        // 后台创建的用户固定为后台类型，避免落库走移动端默认值
        entity.setType(UserTypeEnum.ADMIN.getType());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        this.save(entity);

        entity.setPassword(null);
        return entity;
    }

    @Override
    public void remove(String id) {
        boolean one = this.removeById(id);
        CRUDUtil.validateDeleteSuccess(one);
        RedisUtils.delete(RedisKey.getKeyNoTenant(RedisKey.USER_INFO, id));
    }

    @Override
    public User update(UserUpdateDTO dto) {
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        CRUDUtil.validateUpdateSuccess(this.updateById(entity));
        // 刷新登录缓存，禁用后由拦截器按 status 拦截提示
        this.refreshUserInfoCache(dto.getId());
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
        LoginStrategy strategy = loginStrategyContext.getStrategy(dto.getType());
        return strategy.execute(dto);
    }

    @Override
    public Map<String, String> renewal(String refreshToken) {
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
        // 用户已被删除或禁用时拒绝续期
        User dbUser = this.getById(userId);
        if (Objects.isNull(dbUser)) {
            throw new BusinessException(CommonErrorEnum.NOT_LOGIN_ERROR);
        }
        if (Boolean.FALSE.equals(dbUser.getStatus())) {
            throw new BusinessException(CommonErrorEnum.FORBIDDEN_ERROR);
        }
        // 生成新的 AccessToken
        Map<String, String> m = Map.of("userId", userId, "deviceFingerprint", deviceFingerprint);
        String accessToken = JwtUtils.generateToken(m, true);
        refreshToken = JwtUtils.generateToken(m, false);
        // 存储到 redis
        RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId, deviceFingerprint), accessToken, LoginConstant.ACCESS_TOKEN_EXPIRE, TimeUnit.MINUTES);
        RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId, deviceFingerprint), refreshToken, LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);
        RedisUtils.expire(RedisKey.getKeyNoTenant(RedisKey.USER_INFO, userId), LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);

        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    @Override
    public void logout() {
        UserInfo user = (UserInfo) UserHolder.get();
        String userId = user.getId();
        String deviceFingerprint = user.getDeviceFingerprint();
        RedisUtils.delete(RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId, deviceFingerprint));
        RedisUtils.delete(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId, deviceFingerprint));
        RedisUtils.delete(RedisKey.getKeyNoTenant(RedisKey.USER_INFO, userId));
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
    public Set<String> listPermissionCodes(String userId) {
        return CollStreamUtils.toSet(this.listUserPermissions(userId, null), UserPermissionVO::getCode);
    }

    @Override
    public void refreshPermissionCacheByUserIds(Collection<String> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        for (String userId : userIds) {
            String key = RedisKey.getKeyNoTenant(RedisKey.USER_INFO, userId);
            UserInfo cached = RedisUtils.get(key, UserInfo.class);
            // 离线用户无缓存，下次登录时加载最新权限
            if (Objects.isNull(cached)) {
                continue;
            }
            cached.setPermissions(this.listPermissionCodes(userId));
            RedisUtils.set(key, cached, LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);
        }
    }

    @Override
    public void refreshPermissionCacheByRoleIds(Collection<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        List<UserRole> userRoleList = userRoleService.lambdaQuery().in(UserRole::getRoleId, roleIds).list();
        this.refreshPermissionCacheByUserIds(CollStreamUtils.toDistinctList(userRoleList, UserRole::getUserId));
    }

    @Override
    public void refreshPermissionCacheByPermissionIds(Collection<String> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        List<RolePermission> rolePermissionList = rolePermissionService.lambdaQuery().in(RolePermission::getPermissionId, permissionIds).list();
        this.refreshPermissionCacheByRoleIds(CollStreamUtils.toDistinctList(rolePermissionList, RolePermission::getRoleId));
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

    /**
     * 刷新用户登录缓存（保留设备指纹）；用户未登录（无缓存）时无需处理
     */
    private void refreshUserInfoCache(String userId) {
        String key = RedisKey.getKeyNoTenant(RedisKey.USER_INFO, userId);
        UserInfo cached = RedisUtils.get(key, UserInfo.class);
        if (Objects.isNull(cached)) {
            return;
        }
        User latest = this.getById(userId);
        if (Objects.isNull(latest)) {
            RedisUtils.delete(key);
            return;
        }
        BeanUtils.copyProperties(latest, cached);
        RedisUtils.set(key, cached, LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);
    }
}