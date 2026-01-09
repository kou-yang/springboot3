package com.conggua.springboot3.server.service;

import com.conggua.common.web.model.response.CommonPage;
import com.conggua.common.web.service.BaseService;
import com.conggua.springboot3.server.model.dto.PermissionPageDTO;
import com.conggua.springboot3.server.model.dto.PermissionSaveDTO;
import com.conggua.springboot3.server.model.dto.PermissionUpdateDTO;
import com.conggua.springboot3.server.model.entity.Permission;
import com.conggua.springboot3.server.model.vo.PermissionDetailVO;
import com.conggua.springboot3.server.model.vo.PermissionPageVO;

/**
 * @author kouyang
 * @since 2026-01-09
 */
public interface PermissionService extends BaseService<Permission> {

    /**
     * 保存
     * @param dto
     */
    Permission save(PermissionSaveDTO dto);

    /**
     * 删除
     * @param id
     */
    void remove(String id);

    /**
     * 修改
     * @param dto
     */
    Permission update(PermissionUpdateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<PermissionPageVO> page(PermissionPageDTO dto);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    PermissionDetailVO getDetail(String id);
}
