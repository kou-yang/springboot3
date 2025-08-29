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
import lombok.Data;
import java.io.Serial;

/**
 * @author ${author}
 * @since ${date}
 */
@Data
public class ${entity}SaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

<#-- ---------- 字段循环遍历 ---------->
<#list table.fields as field>
<#-- 排除主键字段、创建时间、更新时间 -->
    <#if !field.keyFlag && field.name != "create_time" && field.name != "update_time">
        <#if field.comment!?length gt 0>
    @Schema(description = "${field.comment}")
        </#if>
    private ${field.propertyType} ${field.propertyName};
        <#if field_has_next>

        </#if>
    </#if>
</#list>
}