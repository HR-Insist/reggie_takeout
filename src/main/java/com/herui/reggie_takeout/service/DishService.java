package com.herui.reggie_takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.herui.reggie_takeout.dto.DishDto;
import com.herui.reggie_takeout.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入对应的flavor表数据, 两张表：dish,dish_flavor
    public void saveWithFlavor(DishDto dishDto);
    // 根据Id查询菜品信息及对应的口味信息
    public DishDto getByIdWithFlavor(Long id);
    // 更新菜品信息和口味
    void updateWithFlavor(DishDto dishDto);

    void deleteWithFlavor(List<Long> ids);
}
