package com.conggua.springboot3.server.service;

import com.conggua.common.web.model.response.CommonPage;
import com.conggua.common.web.service.BaseService;
import com.conggua.springboot3.server.model.dto.DistributePermissionDTO;
import com.conggua.springboot3.server.model.dto.RolePermissionPageDTO;
import com.conggua.springboot3.server.model.dto.RolePermissionSaveDTO;
import com.conggua.springboot3.server.model.dto.RolePermissionUpdateDTO;
import com.conggua.springboot3.server.model.entity.RolePermission;
import com.conggua.springboot3.server.model.vo.RolePermissionDetailVO;
import com.conggua.springboot3.server.model.vo.RolePermissionPageVO;

/**
 * @author kouyang
 * @since 2026-01-09
 */
public interface RolePermissionService extends BaseService<RolePermission> {

    /**
     * 保存
     * @param dto
     */
    RolePermission save(RolePermissionSaveDTO dto);

    /**
     * 删除
     * @param id
     */
    void remove(String id);

    /**
     * 修改
     * @param dto
     */
    RolePermission update(RolePermissionUpdateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<RolePermissionPageVO> page(RolePermissionPageDTO dto);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    RolePermissionDetailVO getDetail(String id);

    /**
     * 分配权限
     * @param dto
     */
    void distribute(DistributePermissionDTO dto);
}
