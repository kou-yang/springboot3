package ${package.Controller};

import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.base.exception.CommonErrorEnum;
import com.conggua.common.web.model.request.PrimaryKeyDTO;
import com.conggua.common.web.model.response.CommonPage;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import ${package.Parent}.model.dto.${entity}SaveDTO;
import ${package.Parent}.model.dto.${entity}UpdateDTO;
import ${package.Parent}.model.dto.${entity}PageDTO;
import ${package.Parent}.model.vo.${entity}PageVO;
import ${package.Parent}.model.vo.${entity}DetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ${author}
 * @since ${date}
 */
@Tag(name = "${table.comment!}管理")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@RequiredArgsConstructor
<#if kotlin>
    class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>

    private final ${table.serviceName} ${table.entityPath}Service;

    @Operation(summary = "保存")
    @PostMapping("/save")
    public Result<?> save(@Validated @RequestBody ${entity}SaveDTO dto) {
        boolean success = ${table.entityPath}Service.save(dto);
        return ResultUtils.ofSuccess(success, CommonErrorEnum.SAVE_ERROR);
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody PrimaryKeyDTO dto) {
        boolean success = ${table.entityPath}Service.removeById(dto.id());
        return ResultUtils.ofSuccess(success, CommonErrorEnum.DELETE_ERROR);
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<?> update(@Validated @RequestBody ${entity}UpdateDTO dto) {
        boolean success = ${table.entityPath}Service.update(dto);
        return ResultUtils.ofSuccess(success, CommonErrorEnum.UPDATE_ERROR);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public Result<CommonPage<${entity}PageVO>> page(@Validated @RequestBody ${entity}PageDTO dto) {
        CommonPage<${entity}PageVO> page = ${table.entityPath}Service.page(dto);
        return ResultUtils.success(page);
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("/detail/{id}")
    public Result<${entity}DetailVO> getDetail(@PathVariable String id) {
        ${entity}DetailVO vo = ${table.entityPath}Service.getDetal(id);
        return ResultUtils.success(vo);
    }

    @Operation(summary = "查询全部")
    @GetMapping("/all")
    public Result<List<${entity}>> listAll() {
        List<${entity}> list = ${table.entityPath}Service.list();
        return ResultUtils.success(list);
    }
}
</#if>