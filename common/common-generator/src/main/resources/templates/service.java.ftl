package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import ${package.Parent}.model.dto.${entity}SaveDTO;
import ${package.Parent}.model.dto.${entity}UpdateDTO;
import ${package.Parent}.model.dto.${entity}PageDTO;
import ${package.Parent}.model.vo.${entity}PageVO;
import ${package.Parent}.model.vo.${entity}DetailVO;
import com.conggua.common.web.model.response.CommonPage;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    @Override
    default List<${entity}> listByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return ${superServiceClass}.super.listByIds(idList);
    }

    /**
     * 保存
     * @param dto
     */
    boolean save(${entity}SaveDTO dto);

    /**
     * 修改
     * @param dto
     */
    boolean update(${entity}UpdateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<${entity}PageVO> page(${entity}PageDTO dto);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    ${entity}DetailVO getDetal(String id);
}
</#if>