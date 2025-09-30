package com.conggua.common.web.model.request;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.conggua.common.base.util.CollStreamUtils;
import com.github.pagehelper.PageHelper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-02-27 17:14
 */
@Data
public class CommonPageDTO {

    @Schema(description = "页索引", defaultValue = "1")
    private Integer pageIndex = 1;
    @Schema(description = "页大小", defaultValue = "10")
    private Integer pageSize = 10;
    @Schema(description = "排序")
    @Valid
    private List<Sort> sort;

    @Data
    public static class Sort {

        @Schema(description = "排序字段")
        @NotBlank
        private String field;
        @Schema(description = "排序方式", defaultValue = "desc", allowableValues = {"asc", "desc"})
        @NotBlank
        private String order;
    }

    public <T> Page<T> startMpPage(Class<T> clazz) {
        Page<T> page = Page.of(this.pageIndex, this.pageSize);
        // 添加排序
        this.addOrder(page, clazz);
        return page;
    }

    public <T> com.github.pagehelper.Page<T> startPhPage(Class<T> clazz) {
        com.github.pagehelper.Page<T> page = PageHelper.startPage(this.pageIndex, this.pageSize);
        String orderSql = this.buildOrderSql(clazz);
        if (StringUtils.isNotBlank(orderSql)) {
            page.setUnsafeOrderBy(orderSql);
        }
        return page;
    }

    private <T> void addOrder(Page<T> page, Class<T> clazz) {
        if (CollectionUtils.isEmpty(this.sort)) {
            return;
        }
        // 获取所有字段
        List<Field> fieldList = FieldUtils.getAllFieldsList(clazz);
        List<String> fieldNameList = CollStreamUtils.toList(fieldList, Field::getName);

        for (CommonPageDTO.Sort s : this.sort) {
            if (StringUtils.isAnyBlank(s.getField(), s.getOrder())) {
                continue;
            }
            // 跳过非法字段（防止 SQL 注入）
            if (!fieldNameList.contains(s.getField())) {
                continue;
            }
            boolean isAsc = "asc".equalsIgnoreCase(s.getOrder());
            String column = s.getField().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            page.addOrder(isAsc ? OrderItem.asc(column) : OrderItem.desc(column));
        }
    }

    private  <T> String buildOrderSql(Class<T> clazz) {
        if (CollectionUtils.isEmpty(this.sort)) {
            return "";
        }
        // 获取所有字段
        List<Field> fieldList = FieldUtils.getAllFieldsList(clazz);
        List<String> fieldNameList = CollStreamUtils.toList(fieldList, Field::getName);

        List<String> orders = new ArrayList<>();
        for (Sort s : this.sort) {
            if (StringUtils.isAnyBlank(s.getField(), s.getOrder())) {
                continue;
            }
            // 跳过非法字段（防止 SQL 注入）
            if (!fieldNameList.contains(s.getField())) {
                continue;
            }
            String column = s.getField().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            String direction = "asc".equalsIgnoreCase(s.getOrder()) ? "ASC" : "DESC";
            orders.add(column + " " + direction);
        }

        return orders.isEmpty() ? "" : String.join(", ", orders);
    }
}
