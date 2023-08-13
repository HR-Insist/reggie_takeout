package com.herui.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.herui.reggie_takeout.dto.DishDto;
import com.herui.reggie_takeout.mapper.DishMapper;
import com.herui.reggie_takeout.pojo.Dish;
import com.herui.reggie_takeout.pojo.DishFlavor;
import com.herui.reggie_takeout.service.DishFlavorService;
import com.herui.reggie_takeout.service.DishService;
import com.herui.reggie_takeout.utils.UserIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    private final DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品基本信息到菜品表
        this.save(dishDto);
        Long id = dishDto.getId(); // 菜品id

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().peek(item-> item.setDishId(id)).collect(Collectors.toList());
        // 保存菜品口味数据到dish_flavor表
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
        // 查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表
        this.updateById(dishDto);

        // 清理菜品对应口味数据 dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 添加新口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    @Transactional
    public void deleteWithFlavor(List<Long> ids) {
        // 删除dish
        this.removeByIds(ids);
        // 删除dish对应的flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(queryWrapper);
    }


}
