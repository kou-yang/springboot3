package ${saveDTOPackage};

<#list table.importPackages as pkg>
    <#if !pkg?starts_with("com.baomidou.mybatisplus") &&
    !pkg?starts_with("java.time.LocalDateTime")>
import ${pkg};
    </#if>
</#list>
<#-- 检查是否需要导入LocalDateTime -->
<#assign hasLocalDateTime = false>
<#list table.fields as field>
    <#if field.propertyType == "LocalDateTime" && !hasLocalDateTime>
        <#assign hasLocalDateTime = true>
    </#if>
</#list>
<#if hasLocalDateTime>
import java.time.LocalDateTime;
</#if>
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serial;

/**
 * @author ${author}
 * @since ${date}
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ${entity}SaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
    @Schema(description = "${field.comment}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
    <#if field_has_next>

    </#if>
</#list>
}