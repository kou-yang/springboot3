package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import ${package.Parent}.model.dto.${entity}SaveDTO;
import ${package.Parent}.model.dto.${entity}UpdateDTO;
import ${package.Parent}.model.dto.${entity}PageDTO;
import ${package.Parent}.model.vo.${entity}VO;
import com.conggua.common.web.model.response.CommonPage;

import java.util.List;

/**
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * 保存
     * @param dto
     */
    void save(${entity}SaveDTO dto);

    /**
     * 修改
     * @param dto
     */
    void update(${entity}UpdateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<${entity}VO> page(${entity}PageDTO dto);

    /**
     * 实体转VO
     * @param entityList
     * @return
     */
    List<${entity}VO> entityList2VOList(List<${entity}> entityList);

    /**
     * 实体转VO
     * @param entity
     * @return
     */
    ${entity}VO entity2VO(${entity} entity);
}
</#if>