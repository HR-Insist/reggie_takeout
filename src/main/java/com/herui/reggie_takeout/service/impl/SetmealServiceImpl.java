package com.herui.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.herui.reggie_takeout.dto.SetmealDto;
import com.herui.reggie_takeout.exception.CustomException;
import com.herui.reggie_takeout.mapper.SetmealMapper;
import com.herui.reggie_takeout.pojo.Setmeal;
import com.herui.reggie_takeout.pojo.SetmealDish;
import com.herui.reggie_takeout.service.SetmealDishService;
import com.herui.reggie_takeout.service.SetmealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    private final SetmealDishService setmealDishService;
    /**
     * 新增套餐，同时保存setmeal_dish 表
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);
        // 保存套餐和菜品关联信息，操作setmeal_dish 执行insert操作

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> list = setmealDishes.stream().peek((item -> item.setSetmealId(setmealDto.getId()))).toList();
        setmealDishService.saveBatch(list);

    }

    @Override
    public SetmealDto getWithDishById(Long id) {
        // 根据ID查询套餐基本信息
        Setmeal setmeal = this.getById(id);

        // 获取套餐中的菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> dishes = setmealDishService.list(queryWrapper);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        setmealDto.setSetmealDishes(dishes);

        return setmealDto;


    }

    @Override
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> list = setmealDishes.stream().peek((item -> item.setSetmealId(setmealDto.getId()))).toList();
        setmealDishService.saveBatch(list);

    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 判断是否存在售卖中的套餐
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1);
        long count = this.count(queryWrapper);
        if (count > 0){
            throw new CustomException("套餐正在售卖，不能删除");
        }
        // 如果可以删除的话，删除 setmeal
        this.removeByIds(ids);
        // 再删除setmeal_dish
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

}
