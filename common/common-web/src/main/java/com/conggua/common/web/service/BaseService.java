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
     * 根据字段 EQ 查询单个数据
     * @param column
     * @param value
     * @return
     */
    default T getBy(SFunction<T, ?> column, Object value) {
        return lambdaQuery().eq(column, value).one();
    }

    /**
     * 根据字段 EQ 查询集合数据
     * @param column
     * @param value
     * @return
     */
    default List<T> listBy(SFunction<T, ?> column, Object value) {
        return lambdaQuery().eq(column, value).list();
    }

    /**
     * 根据字段 IN 查询集合数据
     * @param column
     * @param values
     * @return
     */
    default List<T> listIn(SFunction<T, ?> column, List<Object> values) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(column, values).list();
    }

    /**
     * 根据字段 EQ 删除数据
     * @param column
     * @param value
     * @return
     */
    default boolean removeBy(SFunction<T, ?> column, Object value) {
        return lambdaUpdate().eq(column, value).remove();
    }
}
