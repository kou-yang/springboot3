package com.conggua.springboot3.server.service;

import com.conggua.common.web.model.response.CommonPage;
import com.conggua.common.web.service.BaseService;
import com.conggua.springboot3.server.model.dto.UserLoginDTO;
import com.conggua.springboot3.server.model.dto.UserPageDTO;
import com.conggua.springboot3.server.model.dto.UserSaveDTO;
import com.conggua.springboot3.server.model.dto.UserUpdateDTO;
import com.conggua.springboot3.server.model.entity.User;
import com.conggua.springboot3.server.model.vo.UserDetailVO;
import com.conggua.springboot3.server.model.vo.UserPageVO;
import com.conggua.springboot3.server.model.vo.UserPermissionVO;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

/**
 * @author kouyang
 * @since 2026-02-02
 */
public interface UserService extends BaseService<User> {

    /**
     * 保存
     * @param dto
     */
    User save(UserSaveDTO dto);

    /**
     * 删除
     * @param id
     */
    void remove(String id);

    /**
     * 修改
     * @param dto
     */
    User update(UserUpdateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<UserPageVO> page(UserPageDTO dto);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    UserDetailVO getDetail(String id);

    /**
     * 登录
     * @param dto
     * @return
     */
    Map<String, String> login(UserLoginDTO dto);

    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    String renewal(@NotBlank String refreshToken);

    /**
     * 登出
     */
    void logout();

    /**
     * 获取用户权限
     * @param userId 用户ID
     * @param type 权限类型
     * @return
     */
    List<UserPermissionVO> listUserPermissions(String userId, String type);
}
