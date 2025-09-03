package com.conggua.common.web.model.request;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.conggua.common.base.util.CollStreamUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-02-27 17:14
 */
@Data
public class CommonPageDTO {

    @Schema(description = "页索引")
    private Integer pageIndex = 1;
    @Schema(description = "页大小")
    private Integer pageSize = 10;
    @Schema(description = "排序")
    private List<Sort> sort;

    @Data
    public static class Sort {

        @Schema(description = "排序字段")
        private String field;
        @Schema(description = "排序方式")
        private String order;
    }

    public <T> Page<T> addOrder(Page<T> page, List<Sort> sorts, Class<T> clazz) {
        if (CollectionUtils.isEmpty(sorts)) {
            return page;
        }
        // 获取所有字段
        List<Field> fieldList = FieldUtils.getAllFieldsList(clazz);
        List<String> fieldNameList = CollStreamUtils.toList(fieldList, Field::getName);

        for (CommonPageDTO.Sort sort : sorts) {
            if (StringUtils.isAnyBlank(sort.getField(), sort.getOrder())) {
                continue;
            }
            // 跳过非法字段（防止 SQL 注入）
            if (!fieldNameList.contains(sort.getField())) {
                continue;
            }
            boolean isAsc = "asc".equalsIgnoreCase(sort.getOrder());
            String column = sort.getField().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
            if (isAsc) {
                page.addOrder(OrderItem.asc(column));
            } else {
                page.addOrder(OrderItem.desc(column));
            }
        }
        return page;
    }
}
