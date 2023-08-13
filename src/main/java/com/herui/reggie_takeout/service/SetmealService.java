package com.herui.reggie_takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.herui.reggie_takeout.dto.SetmealDto;
import com.herui.reggie_takeout.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    SetmealDto getWithDishById(Long id);

    void updateWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
