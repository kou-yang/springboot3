package com.conggua.common.web.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-09-24 17:33
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 根据id查询
     * @param idList
     * @return
     */
    @Override
    default List<T> listByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return IService.super.listByIds(idList);
    }

    /**
     * 根据字段查询
     * @param column
     * @param value
     * @return
     */
    default T getBy(SFunction<T, ?> column, Object value) {
        return lambdaQuery().eq(column, value).one();
    }

    /**
     * 根据字段查询
     * @param column
     * @param value
     * @return
     */
    default List<T> listBy(SFunction<T, ?> column, Object value) {
        return lambdaQuery().eq(column, value).list();
    }
}
