package ${voPackage};

<#list table.importPackages as pkg>
    <#if !pkg?starts_with("com.baomidou.mybatisplus")>
import ${pkg};
    </#if>
</#list>
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serial;

/**
 * @author ${author}
 * @since ${date}
 */
@Data
public class ${entity}VO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

<#-- ---------- 字段循环遍历 ---------->
<#list table.fields as field>
    <#if field.comment!?length gt 0>
    @Schema(description = "${field.comment}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
    <#if field_has_next>

    </#if>
</#list>
}