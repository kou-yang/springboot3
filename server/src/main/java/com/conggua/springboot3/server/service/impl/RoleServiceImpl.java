package com.conggua.springboot3.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.util.CRUDUtil;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.base.util.SpringContextUtils;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.mapper.RoleMapper;
import com.conggua.springboot3.server.model.dto.RolePageDTO;
import com.conggua.springboot3.server.model.dto.RoleSaveDTO;
import com.conggua.springboot3.server.model.dto.RoleUpdateDTO;
import com.conggua.springboot3.server.model.entity.Role;
import com.conggua.springboot3.server.model.entity.RolePermission;
import com.conggua.springboot3.server.model.vo.RoleDetailVO;
import com.conggua.springboot3.server.model.vo.RolePageVO;
import com.conggua.springboot3.server.service.RolePermissionService;
import com.conggua.springboot3.server.service.RoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Role save(RoleSaveDTO dto) {
        Role role = getBy(Role::getCode, dto.getCode());
        if (role != null) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "角色编码已存在");
        }
        Role entity = new Role();
        BeanUtils.copyProperties(dto, entity);
        this.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(String id) {
        boolean one = this.removeById(id);
        CRUDUtil.validateDeleteSuccess(one);
        // 删除分配的权限
        RolePermissionService rolePermissionService = SpringContextUtils.getBean(RolePermissionService.class);
        rolePermissionService.removeBy(RolePermission::getRoleId, id);
    }

    @Override
    public Role update(RoleUpdateDTO dto) {
        Role old = getById(dto.getId());
        if (StringUtils.isBlank(dto.getCode()) && !Objects.equals(old.getCode(), dto.getCode())) {
            Role role = getBy(Role::getCode, dto.getCode());
            if (role != null) {
                throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "角色编码已存在");
            }
        }
        Role entity = new Role();
        BeanUtils.copyProperties(dto, entity);
        this.updateById(entity);
        return entity;
    }

    @Override
    public CommonPage<RolePageVO> page(RolePageDTO dto) {
        Page<Role> page = dto.startMpPage(Role.class);
        page = lambdaQuery()
                .like(StringUtils.isNotBlank(dto.getName()), Role::getName, dto.getName())
                .like(StringUtils.isNotBlank(dto.getCode()), Role::getCode, dto.getCode())
                .page(page);
        // entity转vo
        List<RolePageVO> voList = this.entityList2PageVOList(page.getRecords());
        return CommonPage.restPage(voList, page.getTotal());
    }

    @Override
    public RoleDetailVO getDetail(String id) {
        Role entity = this.getById(id);
        return this.entity2DetailVO(entity);
    }

    public List<RolePageVO> entityList2PageVOList(List<Role> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return CollStreamUtils.toList(entityList, entity -> {
            RolePageVO vo = new RolePageVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
    }

    public RoleDetailVO entity2DetailVO(Role entity) {
        if (entity == null) {
            return null;
        }
        RoleDetailVO vo = new RoleDetailVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}