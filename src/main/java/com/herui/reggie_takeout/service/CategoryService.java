package com.herui.reggie_takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.herui.reggie_takeout.pojo.Category;
import org.springframework.stereotype.Service;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
