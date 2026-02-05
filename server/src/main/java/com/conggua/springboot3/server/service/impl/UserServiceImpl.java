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
import org.springframework.stereotype.Service;

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

    @Override
    public User save(UserSaveDTO dto) {
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        boolean one = this.save(entity);
        CRUDUtil.validateSaveSuccess(one);
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
        boolean one = this.updateById(entity);
        CRUDUtil.validateUpdateSuccess(one);
        return entity;
    }

    @Override
    public CommonPage<UserPageVO> page(UserPageDTO dto) {
        Page<User> page = dto.startMpPage(User.class);
        page = lambdaQuery().page(page);
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
        assert jwt != null;
        Map<String, ?> map = JwtUtils.getMap(jwt);
        String userId = (String) map.get("userId");
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "RefreshToken 错误");
        }
        String latestRefreshToken = RedisUtils.get(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId), String.class);
        if (!Objects.equals(refreshToken, latestRefreshToken)) {
            throw new BusinessException(CommonErrorEnum.NOT_LOGIN_ERROR);
        }
        String accessToken = RedisUtils.get(RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId), String.class);
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        // 生成新的AccessToken
        Map<String, String> m = Map.of("userId", userId);
        accessToken = JwtUtils.generateToken(m, true);
        // 存储redis
        RedisUtils.set(RedisKey.getKeyNoTenant(RedisKey.ACCESS_TOKEN, userId), accessToken, LoginConstant.ACCESS_TOKEN_EXPIRE, TimeUnit.MINUTES);
        // RefreshToken续期
        RedisUtils.expire(RedisKey.getKeyNoTenant(RedisKey.REFRESH_TOKEN, userId), LoginConstant.REFRESH_TOKEN_EXPIRE, TimeUnit.DAYS);
        return accessToken;
    }

    @Override
    public void logout() {
        String userId = UserHolder.getUserId();
        RedisUtils.delete(RedisKey.getKey(RedisKey.ACCESS_TOKEN, userId));
        RedisUtils.delete(RedisKey.getKey(RedisKey.REFRESH_TOKEN, userId));
        RedisUtils.delete(RedisKey.getKey(RedisKey.USER_INFO, userId));
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

    public List<UserPageVO> entityList2PageVOList(List<User> entityList) {
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