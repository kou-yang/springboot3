package com.conggua.springboot3.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.util.CRUDUtil;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.mapper.RolePermissionMapper;
import com.conggua.springboot3.server.model.dto.DistributePermissionDTO;
import com.conggua.springboot3.server.model.dto.RolePermissionPageDTO;
import com.conggua.springboot3.server.model.dto.RolePermissionSaveDTO;
import com.conggua.springboot3.server.model.dto.RolePermissionUpdateDTO;
import com.conggua.springboot3.server.model.entity.Permission;
import com.conggua.springboot3.server.model.entity.Role;
import com.conggua.springboot3.server.model.entity.RolePermission;
import com.conggua.springboot3.server.model.vo.RolePermissionDetailVO;
import com.conggua.springboot3.server.model.vo.RolePermissionPageVO;
import com.conggua.springboot3.server.service.PermissionService;
import com.conggua.springboot3.server.service.RolePermissionService;
import com.conggua.springboot3.server.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author kouyang
 * @since 2026-01-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @Override
    public RolePermission save(RolePermissionSaveDTO dto) {
        // 参数校验
        Role role = roleService.getById(dto.getRoleId());
        Permission permission = permissionService.getById(dto.getPermissionId());
        if (ObjectUtils.anyNull(role, permission)) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "角色或权限不存在");
        }
        // 分配权限
        RolePermission entity = new RolePermission();
        BeanUtils.copyProperties(dto, entity);
        entity.setType(permission.getType());
        this.save(entity);
        return entity;
    }

    @Override
    public void remove(String id) {
        boolean one = this.removeById(id);
        CRUDUtil.validateDeleteSuccess(one);
    }

    @Override
    public RolePermission update(RolePermissionUpdateDTO dto) {
        RolePermission entity = new RolePermission();
        BeanUtils.copyProperties(dto, entity);
        this.updateById(entity);
        return entity;
    }

    @Override
    public CommonPage<RolePermissionPageVO> page(RolePermissionPageDTO dto) {
        Page<RolePermission> page = dto.startMpPage(RolePermission.class);
        page = lambdaQuery()
                .eq(StringUtils.isNotBlank(dto.getType()), RolePermission::getType, dto.getType())
                .eq(StringUtils.isNotBlank(dto.getRoleId()), RolePermission::getRoleId, dto.getRoleId())
                .eq(StringUtils.isNotBlank(dto.getPermissionId()), RolePermission::getPermissionId, dto.getPermissionId())
                .page(page);
        // entity转vo
        List<RolePermissionPageVO> voList = this.entityList2PageVOList(page.getRecords());
        return CommonPage.restPage(voList, page.getTotal());
    }

    @Override
    public RolePermissionDetailVO getDetail(String id) {
        RolePermission entity = this.getById(id);
        return this.entity2DetailVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distribute(DistributePermissionDTO dto) {
        // 参数校验
        Role role = roleService.getById(dto.getRoleId());
        if (role == null) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "角色不存在");
        }
        List<Permission> permissionList = permissionService.listBy(Permission::getType, dto.getType());
        List<String> permissionIds = CollStreamUtils.toList(permissionList, Permission::getId);
        List<String> failPermissionIdList = CollStreamUtils.filterList(dto.getPermissionIds(), permissionId -> !permissionIds.contains(permissionId));
        if (CollectionUtils.isNotEmpty(failPermissionIdList)) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "权限不存在");
        }
        // 分配权限（先删除再添加）
        removeBy(RolePermission::getType, dto.getType(), RolePermission::getRoleId, dto.getRoleId());
        List<RolePermission> list = CollStreamUtils.toList(dto.getPermissionIds(), permissionId -> {
            RolePermission entity = new RolePermission();
            entity.setType(dto.getType());
            entity.setRoleId(dto.getRoleId());
            entity.setPermissionId(permissionId);
            return entity;
        });
        boolean one = saveBatch(list);
        CRUDUtil.validateSaveSuccess(one);
    }

    public List<RolePermissionPageVO> entityList2PageVOList(List<RolePermission> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        Map<String, String> roleNameMap = CollStreamUtils.toMap(roleService.list(), Role::getId, Role::getName);
        Map<String, String> permissionNameMap = CollStreamUtils.toMap(permissionService.list(), Permission::getId, Permission::getName);
        return CollStreamUtils.toList(entityList, entity -> {
            RolePermissionPageVO vo = new RolePermissionPageVO();
            BeanUtils.copyProperties(entity, vo);
            vo.setRoleName(roleNameMap.getOrDefault(entity.getRoleId(), ""));
            vo.setPermissionName(permissionNameMap.getOrDefault(entity.getPermissionId(), ""));
            return vo;
        });
    }

    public RolePermissionDetailVO entity2DetailVO(RolePermission entity) {
        if (entity == null) {
            return null;
        }
        RolePermissionDetailVO vo = new RolePermissionDetailVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}