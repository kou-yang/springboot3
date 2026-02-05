package com.conggua.springboot3.server.datascope;

import com.conggua.common.base.common.UserHolder;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.web.provider.DataScopeProvider;
import com.conggua.springboot3.server.constant.PermissionTypeEnum;
import com.conggua.springboot3.server.model.vo.UserPermissionVO;
import com.conggua.springboot3.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-12-03 15:29
 */
@Component
@RequiredArgsConstructor
public class CustomDataScopeProvider implements DataScopeProvider {

    private final UserService userService;

    @Override
    public List<String> getPermissions() {
        List<UserPermissionVO> dataScopeList = userService.listUserPermissions(UserHolder.getUserId(), PermissionTypeEnum.DATA_SCOPE.getType());
        return CollStreamUtils.toDistinctList(dataScopeList, UserPermissionVO::getCode);
    }
}
