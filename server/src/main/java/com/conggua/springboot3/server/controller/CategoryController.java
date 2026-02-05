package com.conggua.springboot3.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.conggua.common.base.common.Result;
import com.conggua.common.base.common.ResultUtils;
import com.conggua.common.base.util.TreeUtils;
import com.conggua.common.web.model.request.PrimaryKeyDTO;
import com.conggua.common.web.model.response.CommonPage;
import com.conggua.springboot3.server.model.dto.CategoryPageDTO;
import com.conggua.springboot3.server.model.dto.CategorySaveDTO;
import com.conggua.springboot3.server.model.dto.CategoryUpdateDTO;
import com.conggua.springboot3.server.model.entity.Category;
import com.conggua.springboot3.server.model.vo.CategoryDetailVO;
import com.conggua.springboot3.server.model.vo.CategoryPageVO;
import com.conggua.springboot3.server.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author kouyang
 * @since 2026-02-05
 */
@Tag(name = "sys-通用分类管理")
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "保存")
    @PostMapping("/save")
    public Result<?> save(@Validated @RequestBody CategorySaveDTO dto) {
        Category entity = categoryService.save(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "删除")
    @PostMapping("/delete")
    public Result<?> delete(@Validated @RequestBody PrimaryKeyDTO dto) {
        categoryService.remove(dto.id());
        return ResultUtils.success();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public Result<?> update(@Validated @RequestBody CategoryUpdateDTO dto) {
        Category entity = categoryService.update(dto);
        return ResultUtils.success(entity);
    }

    @Operation(summary = "分页查询")
    @PostMapping("/page")
    public Result<CommonPage<CategoryPageVO>> page(@Validated @RequestBody CategoryPageDTO dto) {
        CommonPage<CategoryPageVO> page = categoryService.page(dto);
        return ResultUtils.success(page);
    }

    @Operation(summary = "根据id查询详情")
    @GetMapping("/detail/{id}")
    public Result<CategoryDetailVO> getDetail(@PathVariable String id) {
        CategoryDetailVO vo = categoryService.getDetail(id);
        return ResultUtils.success(vo);
    }

    @Operation(summary = "查询全部")
    @GetMapping("/all")
    public Result<List<Category>> listAll(String module) {
        List<Category> list = categoryService.list(new LambdaQueryWrapper<Category>().eq(Category::getModule, module));
        return ResultUtils.success(TreeUtils.translate(list, Category::getId, Category::getParentId));
    }
}
