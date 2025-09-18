package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${package.Parent}.model.dto.${entity}SaveDTO;
import ${package.Parent}.model.dto.${entity}UpdateDTO;
import ${package.Parent}.model.dto.${entity}PageDTO;
import ${package.Parent}.model.vo.${entity}PageVO;
import ${package.Parent}.model.vo.${entity}DetailVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.web.model.response.CommonPage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public CommonPage<${entity}PageVO> page(${entity}PageDTO dto) {
        Page<${entity}> page = Page.of(dto.getPageIndex(), dto.getPageSize());
        // 添加排序
        page = dto.addOrder(page, dto.getSort(), ${entity}.class);
        page = lambdaQuery().page(page);
        // entity转vo
        List<${entity}PageVO> voList = this.entityList2PageVOList(page.getRecords());
        return CommonPage.restPage(voList, page.getTotal());
    }

    @Override
    public ${entity}DetailVO getDetal(String id) {
        ${entity} entity = this.getById(id);
        return this.entity2DetailVO(entity);
    }

    private List<${entity}PageVO> entityList2PageVOList(List<${entity}> entityList) {
        return CollStreamUtils.toList(entityList, entity -> {
            ${entity}PageVO vo = new ${entity}PageVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
    }

    private ${entity}DetailVO entity2DetailVO(${entity} entity) {
        if (entity == null) {
            return null;
        }
        ${entity}DetailVO vo = new ${entity}DetailVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}