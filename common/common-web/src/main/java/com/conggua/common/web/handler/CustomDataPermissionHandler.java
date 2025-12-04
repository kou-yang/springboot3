package com.conggua.common.web.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.web.annotation.DataScope;
import com.conggua.common.web.constant.DataScopeEnum;
import com.conggua.common.web.provider.DataScopeProvider;
import com.conggua.common.web.threadLocal.DataScopeContext;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author: kouyang
 * @description:
 * @date: 2025-11-05 11:05
 */
@Component
public class CustomDataPermissionHandler implements MultiDataPermissionHandler {

    private static final String NO_PERMISSION_SQL = "1 = 0";

    @Autowired
    private DataScopeProvider provider;

    @SneakyThrows
    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        DataScope dataScope = DataScopeContext.get();
        // 无注解或该表数据不用做数据权限控制，不追加条件
        if (dataScope == null || !Objects.equals(table.getFullyQualifiedName(), dataScope.tableName())) {
            return null;
        }
        if (provider.isAdmin()) {
            return null;
        }
        String departField = dataScope.departField();
        String userField = dataScope.userField();
        // 获取当前用户权限
        List<String> permissions = provider.getPermissions();
        if (CollectionUtils.isEmpty(permissions)) {
            return CCJSqlParserUtil.parseCondExpression(NO_PERMISSION_SQL);
        }
        String sqlSegment = "";
        // 所有数据权限
        if (permissions.contains(DataScopeEnum.ALL.getCode())) {
            return null;
        }
        // 本部门及以下数据权限
        if (permissions.contains(DataScopeEnum.DEPART_CHILDREN.getCode())) {
            // depart_id IN ('1','2','3')
            sqlSegment = buildSqlSegment(departField, provider.getDepartIdsAndChildren());
        }
        // 本部门数据权限
        if (permissions.contains(DataScopeEnum.DEPART.getCode())) {
            // depart_id IN ('1','2','3')
            sqlSegment = buildSqlSegment(departField, provider.getDepartIds());
        }
        // 个人数据权限
        if (permissions.contains(DataScopeEnum.SELF.getCode())) {
            // create_by = '1'
            sqlSegment = userField + " = " + provider.getCurrentUserId();
        }
        return CCJSqlParserUtil.parseCondExpression(sqlSegment);
    }

    private String buildSqlSegment(String field, List<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return NO_PERMISSION_SQL;
        }
        return field + " IN " + CollStreamUtils.join(values, item -> "'" + item + "'", ",", "(", ")");
    }
}
