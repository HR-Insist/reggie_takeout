package com.herui.reggie_takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.herui.reggie_takeout.common.Result;
import com.herui.reggie_takeout.pojo.Category;
import com.herui.reggie_takeout.service.CategoryService;
import com.herui.reggie_takeout.utils.UserIdUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/page")
    public Result page(@RequestParam Integer page, @RequestParam Integer pageSize){
        // 构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        lambdaQueryWrapper.eq(Category::getIsDeleted, 0);
        // 添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        //  执行查询
        categoryService.page(pageInfo, lambdaQueryWrapper);

        return Result.success(pageInfo);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        categoryService.remove(id);
        return Result.success();
    }

    @PostMapping
    public Result add(HttpServletRequest request, @RequestBody Category category){
        category.setCreateUser(UserIdUtil.UserId(request));
        category.setUpdateUser(UserIdUtil.UserId(request));
        category.setIsDeleted(0);
        categoryService.save(category);
        return Result.success();
    }

    @PatchMapping
    public Result edit(HttpServletRequest request, @RequestBody Category category){
        category.setUpdateUser(UserIdUtil.UserId(request));
        categoryService.updateById(category);
        return Result.success("修改分类成功!");
    }

//    category/list?type=1
    @GetMapping("/list")
    public Result typeList(@RequestParam Integer type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type != null, Category::getType, type);
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }



}
