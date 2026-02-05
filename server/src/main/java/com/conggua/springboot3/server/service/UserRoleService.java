package com.conggua.springboot3.server.service;

import com.conggua.common.web.model.response.CommonPage;
import com.conggua.common.web.service.BaseService;
import com.conggua.springboot3.server.model.dto.DistributeRoleDTO;
import com.conggua.springboot3.server.model.dto.UserRolePageDTO;
import com.conggua.springboot3.server.model.dto.UserRoleSaveDTO;
import com.conggua.springboot3.server.model.dto.UserRoleUpdateDTO;
import com.conggua.springboot3.server.model.entity.UserRole;
import com.conggua.springboot3.server.model.vo.UserRoleDetailVO;
import com.conggua.springboot3.server.model.vo.UserRolePageVO;

/**
 * @author kouyang
 * @since 2026-02-02
 */
public interface UserRoleService extends BaseService<UserRole> {

    /**
     * 保存
     * @param dto
     */
    UserRole save(UserRoleSaveDTO dto);

    /**
     * 删除
     * @param id
     */
    void remove(String id);

    /**
     * 修改
     * @param dto
     */
    UserRole update(UserRoleUpdateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<UserRolePageVO> page(UserRolePageDTO dto);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    UserRoleDetailVO getDetail(String id);

    /**
     * 分配角色给用户
     * @param dto
     */
    void distribute(DistributeRoleDTO dto);
}
