package com.conggua.springboot3.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.util.CRUDUtil;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.base.util.SpringContextUtils;
import com.conggua.common.base.util.TreeUtils;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.mapper.PermissionMapper;
import com.conggua.springboot3.server.model.dto.PermissionPageDTO;
import com.conggua.springboot3.server.model.dto.PermissionSaveDTO;
import com.conggua.springboot3.server.model.dto.PermissionUpdateDTO;
import com.conggua.springboot3.server.model.entity.Permission;
import com.conggua.springboot3.server.model.entity.RolePermission;
import com.conggua.springboot3.server.model.vo.PermissionDetailVO;
import com.conggua.springboot3.server.model.vo.PermissionPageVO;
import com.conggua.springboot3.server.model.vo.UserPermissionVO;
import com.conggua.springboot3.server.service.PermissionService;
import com.conggua.springboot3.server.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author kouyang
 * @since 2026-01-09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public Permission save(PermissionSaveDTO dto) {
        Permission permission = getBy(Permission::getType, dto.getType(), Permission::getCode, dto.getCode());
        if (permission != null) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "权限编码已存在");
        }
        Permission entity = new Permission();
        BeanUtils.copyProperties(dto, entity);
        this.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(String id) {
        boolean one = this.removeById(id);
        CRUDUtil.validateDeleteSuccess(one);
        // 删除角色分配的权限
        // 删除分配的权限
        RolePermissionService rolePermissionService = SpringContextUtils.getBean(RolePermissionService.class);
        rolePermissionService.removeBy(RolePermission::getPermissionId, id);
    }

    @Override
    public Permission update(PermissionUpdateDTO dto) {
        Permission old = getById(dto.getId());
        if (StringUtils.isBlank(dto.getCode()) && !Objects.equals(old.getCode(), dto.getCode())) {
            Permission permission = getBy(Permission::getType, old.getType(), Permission::getCode, dto.getCode());
            if (permission != null) {
                throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "权限编码已存在");
            }
        }
        Permission entity = new Permission();
        BeanUtils.copyProperties(dto, entity);
        this.updateById(entity);
        return entity;
    }

    @Override
    public CommonPage<PermissionPageVO> page(PermissionPageDTO dto) {
        Page<Permission> page = dto.startMpPage(Permission.class);
        page = lambdaQuery()
                .eq(StringUtils.isNotBlank(dto.getType()), Permission::getType, dto.getType())
                .eq(dto.getStatus() != null, Permission::getStatus, dto.getStatus())
                .like(StringUtils.isNotBlank(dto.getName()), Permission::getName, dto.getName())
                .like(StringUtils.isNotBlank(dto.getCode()), Permission::getCode, dto.getCode())
                .page(page);
        // entity转vo
        List<PermissionPageVO> voList = this.entityList2PageVOList(page.getRecords());
        return CommonPage.restPage(voList, page.getTotal());
    }

    @Override
    public PermissionDetailVO getDetail(String id) {
        Permission entity = this.getById(id);
        return this.entity2DetailVO(entity);
    }

    @Override
    public List<UserPermissionVO> all(String type, Boolean status) {
        List<Permission> list = lambdaQuery()
                .eq(StringUtils.isNotBlank(type), Permission::getType, type)
                .eq(status != null, Permission::getStatus, status)
                .list();
        List<UserPermissionVO> voList = CollStreamUtils.toList(list, entity -> {
            UserPermissionVO vo = new UserPermissionVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
        return TreeUtils.translate(voList, UserPermissionVO::getId, UserPermissionVO::getParentId);
    }

    public List<PermissionPageVO> entityList2PageVOList(List<Permission> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return CollStreamUtils.toList(entityList, entity -> {
            PermissionPageVO vo = new PermissionPageVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
    }

    public PermissionDetailVO entity2DetailVO(Permission entity) {
        if (entity == null) {
            return null;
        }
        PermissionDetailVO vo = new PermissionDetailVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}