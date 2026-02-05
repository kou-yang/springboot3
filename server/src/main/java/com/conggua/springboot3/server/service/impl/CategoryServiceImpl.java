package com.conggua.springboot3.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conggua.common.base.util.CRUDUtil;
import com.conggua.common.base.util.CollStreamUtils;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.mapper.CategoryMapper;
import com.conggua.springboot3.server.model.dto.CategoryPageDTO;
import com.conggua.springboot3.server.model.dto.CategorySaveDTO;
import com.conggua.springboot3.server.model.dto.CategoryUpdateDTO;
import com.conggua.springboot3.server.model.entity.Category;
import com.conggua.springboot3.server.model.vo.CategoryDetailVO;
import com.conggua.springboot3.server.model.vo.CategoryPageVO;
import com.conggua.springboot3.server.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kouyang
 * @since 2026-02-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public Category save(CategorySaveDTO dto) {
        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);
        boolean one = this.save(entity);
        CRUDUtil.validateSaveSuccess(one);
        return entity;
    }

    @Override
    public void remove(String id) {
        boolean one = this.removeById(id);
        CRUDUtil.validateDeleteSuccess(one);
    }

    @Override
    public Category update(CategoryUpdateDTO dto) {
        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);
        boolean one = this.updateById(entity);
        CRUDUtil.validateUpdateSuccess(one);
        return entity;
    }

    @Override
    public CommonPage<CategoryPageVO> page(CategoryPageDTO dto) {
        Page<Category> page = dto.startMpPage(Category.class);
        page = lambdaQuery().eq(dto.getModule() != null, Category::getModule, dto.getModule())
                .like(dto.getName() != null, Category::getName, dto.getName())
                .page(page);
        // entity转vo
        List<CategoryPageVO> voList = this.entityList2PageVOList(page.getRecords());
        return CommonPage.restPage(voList, page.getTotal());
    }

    @Override
    public CategoryDetailVO getDetail(String id) {
        Category entity = this.getById(id);
        return this.entity2DetailVO(entity);
    }

    public List<CategoryPageVO> entityList2PageVOList(List<Category> entityList) {
        return CollStreamUtils.toList(entityList, entity -> {
            CategoryPageVO vo = new CategoryPageVO();
            BeanUtils.copyProperties(entity, vo);
            return vo;
        });
    }

    public CategoryDetailVO entity2DetailVO(Category entity) {
        if (entity == null) {
            return null;
        }
        CategoryDetailVO vo = new CategoryDetailVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}