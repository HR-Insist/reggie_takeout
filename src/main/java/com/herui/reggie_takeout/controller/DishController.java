package com.herui.reggie_takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.herui.reggie_takeout.common.Result;
import com.herui.reggie_takeout.dto.DishDto;
import com.herui.reggie_takeout.pojo.Category;
import com.herui.reggie_takeout.pojo.Dish;
import com.herui.reggie_takeout.pojo.Employee;
import com.herui.reggie_takeout.service.CategoryService;
import com.herui.reggie_takeout.service.DishFlavorService;
import com.herui.reggie_takeout.service.DishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;
    private final DishFlavorService dishFlavorService;
    private final CategoryService categoryService;

    @GetMapping("/page")
    public Result getDishPage(@RequestParam Integer page, @RequestParam Integer pageSize,
                              @RequestParam(required = false) String name){

        log.info("page={}, pageSize={}, name={}", page, pageSize, name);
        // 构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dtoPage = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        lambdaQueryWrapper.like(name!=null, Dish::getName, name);
        lambdaQueryWrapper.eq(Dish::getIsDeleted, 0);
        // 添加排序条件
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        //  执行查询
        dishService.page(pageInfo, lambdaQueryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtoList = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).toList();
        dtoPage.setRecords(dishDtoList);
        return Result.success(dtoPage);
    }

    @PostMapping
    public Result save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return Result.success("新增菜品成功!");
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }

    @PatchMapping
    public Result update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info(ids.toString());
        dishService.deleteWithFlavor(ids);
        return Result.success();
    }

    @PatchMapping("/status/{status}")
    public Result editStatus(@RequestParam List<Long> ids, @PathVariable Integer status){
        log.info("修改ids为：{}的菜品状态为{}",ids, status);
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Dish::getId, ids);
        updateWrapper.set(Dish::getStatus, status);
        dishService.update(updateWrapper);
        return Result.success();
    }

    @GetMapping("/list")
    public Result getDishByCategoryId(@RequestParam(required = false) Long categoryId,
                                      @RequestParam(required = false) String name){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
        queryWrapper.like(name!=null, Dish::getName, name);
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return Result.success(list);
    }
}
