package com.herui.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.herui.reggie_takeout.exception.CustomException;
import com.herui.reggie_takeout.mapper.CategoryMapper;
import com.herui.reggie_takeout.pojo.Category;
import com.herui.reggie_takeout.pojo.Dish;
import com.herui.reggie_takeout.pojo.Setmeal;
import com.herui.reggie_takeout.service.CategoryService;
import com.herui.reggie_takeout.service.DishService;
import com.herui.reggie_takeout.service.SetmealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final DishService dishService;
    private final SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        // 判断分类是否关联了菜品
        long dishCount = dishService.count(dishQueryWrapper);
        if(dishCount > 0){
            log.info("当前分类下有菜品，暂时无法删除!");
            throw new CustomException("当前分类下有菜品，暂时无法删除!");
        }
        // 是否关联了套餐
        LambdaQueryWrapper<Setmeal> setQueryWrapper = new LambdaQueryWrapper<>();
        setQueryWrapper.eq(Setmeal::getCategoryId, id);
        long setCount = setmealService.count(setQueryWrapper);
        if(setCount > 0){
            log.info("当前套餐下有菜品，暂时无法删除!");
            throw new CustomException("当前套餐下有菜品，暂时无法删除!");
        }
        // 删除
        super.removeById(id);
    }
}
