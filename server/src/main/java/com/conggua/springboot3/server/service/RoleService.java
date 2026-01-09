package com.conggua.springboot3.server.service;

import com.conggua.common.web.model.response.CommonPage;
import com.conggua.common.web.service.BaseService;
import com.conggua.springboot3.server.model.dto.RolePageDTO;
import com.conggua.springboot3.server.model.dto.RoleSaveDTO;
import com.conggua.springboot3.server.model.dto.RoleUpdateDTO;
import com.conggua.springboot3.server.model.entity.Role;
import com.conggua.springboot3.server.model.vo.RoleDetailVO;
import com.conggua.springboot3.server.model.vo.RolePageVO;

/**
 * @author kouyang
 * @since 2026-01-09
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 保存
     * @param dto
     */
    Role save(RoleSaveDTO dto);

    /**
     * 删除
     * @param id
     */
    void remove(String id);

    /**
     * 修改
     * @param dto
     */
    Role update(RoleUpdateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<RolePageVO> page(RolePageDTO dto);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    RoleDetailVO getDetail(String id);
}
