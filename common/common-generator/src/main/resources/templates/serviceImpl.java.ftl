package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${package.Parent}.model.dto.${entity}SaveDTO;
import ${package.Parent}.model.dto.${entity}UpdateDTO;
import ${package.Parent}.model.dto.${entity}PageDTO;
import ${package.Parent}.model.vo.${entity}VO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.web.model.response.CommonPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author ${author}
 * @since ${date}
 */
@Service
@RequiredArgsConstructor
public class ${table.serviceImplName} extends ServiceImpl<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public boolean save(${entity}SaveDTO dto) {
        ${entity} entity = new ${entity}();
        BeanUtils.copyProperties(dto, entity);
        return this.save(entity);
    }

    @Override
    public boolean update(${entity}UpdateDTO dto) {
        ${entity} entity = new ${entity}();
        BeanUtils.copyProperties(dto, entity);
        return this.updateById(entity);
    }

    @Override
    public CommonPage<${entity}VO> page(${entity}PageDTO dto) {
        Page<${entity}> page = Page.of(dto.getPageIndex(), dto.getPageSize());
        // 添加排序
        page = dto.addOrder(page, dto.getSort(), ${entity}.class);
        page = lambdaQuery().page(page);
        // entity转vo
        List<${entity}VO> voList = this.entityList2VOList(page.getRecords());
        return CommonPage.restPage(voList, page.getTotal());
    }

    @Override
    public List<${entity}VO> entityList2VOList(List<${entity}> entityList) {
        return CollStreamUtils.toList(entityList, entity -> {
            ${entity}VO vo = new ${entity}VO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
    }

    @Override
    public ${entity}VO entity2VO(${entity} entity) {
        return Optional.ofNullable(this.entityList2VOList(Collections.singletonList(entity)))
        .map(voList -> voList.get(0))
        .orElse(null);
    }
}