package com.conggua.springboot3.server.service;

import com.conggua.common.web.model.response.CommonPage;
import com.conggua.common.web.service.BaseService;
import com.conggua.springboot3.server.model.dto.CategoryPageDTO;
import com.conggua.springboot3.server.model.dto.CategorySaveDTO;
import com.conggua.springboot3.server.model.dto.CategoryUpdateDTO;
import com.conggua.springboot3.server.model.entity.Category;
import com.conggua.springboot3.server.model.vo.CategoryDetailVO;
import com.conggua.springboot3.server.model.vo.CategoryPageVO;

/**
 * @author kouyang
 * @since 2026-02-05
 */
public interface CategoryService extends BaseService<Category> {

    /**
     * 保存
     * @param dto
     */
    Category save(CategorySaveDTO dto);

    /**
     * 删除
     * @param id
     */
    void remove(String id);

    /**
     * 修改
     * @param dto
     */
    Category update(CategoryUpdateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<CategoryPageVO> page(CategoryPageDTO dto);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    CategoryDetailVO getDetail(String id);
}
