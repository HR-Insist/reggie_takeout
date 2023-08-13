package com.herui.reggie_takeout.dto;

import com.herui.reggie_takeout.pojo.Setmeal;
import com.herui.reggie_takeout.pojo.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

}
