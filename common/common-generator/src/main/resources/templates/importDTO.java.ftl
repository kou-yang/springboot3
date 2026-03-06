package ${importDTOPackage};

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.fesod.sheet.annotation.ExcelProperty;

/**
 * @author ${author}
 * @since ${date}
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ${entity}ImportDTO {

<#-- ---------- 字段循环遍历 ---------->
<#-- 先过滤出需要的字段 -->
<#assign validFields = []>
<#list table.fields as field>
    <#if !field.keyFlag && field.name != "create_time" && field.name != "update_time" && field.name != "create_by" && field.name != "update_by" && field.name != "deleted">
        <#assign validFields = validFields + [field]>
    </#if>
</#list>
<#list validFields as field>
    <#if field.comment!?length gt 0>
    @ExcelProperty(value = "${field.comment}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
    <#if field_has_next>

    </#if>
</#list>
}