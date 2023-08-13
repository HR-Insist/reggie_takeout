package com.herui.reggie_takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.herui.reggie_takeout.common.Result;
import com.herui.reggie_takeout.dto.DishDto;
import com.herui.reggie_takeout.dto.SetmealDto;
import com.herui.reggie_takeout.pojo.Category;
import com.herui.reggie_takeout.pojo.Setmeal;
import com.herui.reggie_takeout.pojo.SetmealDish;
import com.herui.reggie_takeout.service.CategoryService;
import com.herui.reggie_takeout.service.SetmealDishService;
import com.herui.reggie_takeout.service.SetmealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/setmeal")
public class SetmealController {

    private final SetmealDishService setmealDishService;

    private final SetmealService setmealService;
    private final CategoryService categoryService;

    @GetMapping("/page")
    public Result page(@RequestParam Integer page, @RequestParam Integer pageSize,
                       @RequestParam(required = false) String name){
        log.info("page={}, pageSize={}, name={}", page, pageSize, name);

        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, queryWrapper);

        BeanUtils.copyProperties(setmealPage,setmealDtoPage);
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> dishDtoList = records.stream().map(item -> {
            SetmealDto dto = new SetmealDto();
            BeanUtils.copyProperties(item, dto);
            // 获取分类name
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dto.setCategoryName(categoryName);
            // 获取套菜下的菜品
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, item.getId());
            List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishLambdaQueryWrapper);
            dto.setSetmealDishes(setmealDishes);
            return dto;
        }).toList();
        setmealDtoPage.setRecords(dishDtoList);

        return Result.success(setmealDtoPage);
    }

    @PostMapping
    public Result add(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return Result.success("添加套餐成功!");
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getWithDishById(id);
        return Result.success(setmealDto);
    }

    @PutMapping
    public Result update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info(ids.toString());
        setmealService.removeWithDish(ids);
        return Result.success("套餐删除成功!");
    }

}
