package com.conggua.common.web.provider;

import com.conggua.common.base.common.UserHolder;

import java.util.List;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-11-05 11:39
 */
public interface DataScopeProvider {

    /**
     * 获取当前用户数据权限列表
     * @return
     */
    default List<String> getPermissions() {
        return List.of();
    }

    /**
     * 是否是超级管理员
     * @return
     */
    default boolean isAdmin() {
        return false;
    }

    /**
     * 获取当前用户ID
     * @return
     */
    default String getCurrentUserId() {
        return UserHolder.getUserId();
    }

    /**
     * 获取用户部门
     * @return
     */
    default List<String> getDepartIds() {
        return List.of();
    }

    /**
     * 获取用户部门及子部门
     * @return
     */
    default List<String> getDepartIdsAndChildren() {
        return List.of();
    }
}
