package com.conggua.springboot3.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.exception.BusinessException;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.base.util.CRUDUtil;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.mapper.UserRoleMapper;
import com.conggua.springboot3.server.model.dto.DistributeRoleDTO;
import com.conggua.springboot3.server.model.dto.UserRolePageDTO;
import com.conggua.springboot3.server.model.dto.UserRoleSaveDTO;
import com.conggua.springboot3.server.model.dto.UserRoleUpdateDTO;
import com.conggua.springboot3.server.model.entity.Role;
import com.conggua.springboot3.server.model.entity.UserRole;
import com.conggua.springboot3.server.model.vo.UserRoleDetailVO;
import com.conggua.springboot3.server.model.vo.UserRolePageVO;
import com.conggua.springboot3.server.service.RoleService;
import com.conggua.springboot3.server.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author kouyang
 * @since 2026-02-02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final RoleService roleService;

    @Override
    public UserRole save(UserRoleSaveDTO dto) {
        UserRole entity = new UserRole();
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
    public UserRole update(UserRoleUpdateDTO dto) {
        UserRole entity = new UserRole();
        BeanUtils.copyProperties(dto, entity);
        boolean one = this.updateById(entity);
        CRUDUtil.validateUpdateSuccess(one);
        return entity;
    }

    @Override
    public CommonPage<UserRolePageVO> page(UserRolePageDTO dto) {
        Page<UserRole> page = dto.startMpPage(UserRole.class);
        page = lambdaQuery().page(page);
        // entity转vo
        List<UserRolePageVO> voList = this.entityList2PageVOList(page.getRecords());
        return CommonPage.restPage(voList, page.getTotal());
    }

    @Override
    public UserRoleDetailVO getDetail(String id) {
        UserRole entity = this.getById(id);
        return this.entity2DetailVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distribute(DistributeRoleDTO dto) {
        // 参数校验
        List<Role> roleList = roleService.list();
        List<String> allRoleIds = CollStreamUtils.toList(roleList, Role::getId);
        List<String> failRoleIdList = CollStreamUtils.filterList(dto.getRoleIds(), roleId -> !allRoleIds.contains(roleId));
        if (CollectionUtils.isNotEmpty(failRoleIdList)) {
            throw new BusinessException(CommonErrorEnum.PARAMS_ERROR, "角色不存在");
        }
        // 分配角色（先删除再添加）
        removeBy(UserRole::getUserId, dto.getUserId());
        List<UserRole> list = CollStreamUtils.toList(dto.getRoleIds(), roleId -> {
            UserRole entity = new UserRole();
            entity.setUserId(dto.getUserId());
            entity.setRoleId(roleId);
            return entity;
        });
        boolean one = saveBatch(list);
        CRUDUtil.validateSaveSuccess(one);
    }

    public List<UserRolePageVO> entityList2PageVOList(List<UserRole> entityList) {
        return CollStreamUtils.toList(entityList, entity -> {
            UserRolePageVO vo = new UserRolePageVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
    }

    public UserRoleDetailVO entity2DetailVO(UserRole entity) {
        if (entity == null) {
            return null;
        }
        UserRoleDetailVO vo = new UserRoleDetailVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}