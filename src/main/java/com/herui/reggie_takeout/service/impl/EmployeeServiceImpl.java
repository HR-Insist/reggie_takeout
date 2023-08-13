package com.herui.reggie_takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.herui.reggie_takeout.mapper.EmployeeMapper;
import com.herui.reggie_takeout.pojo.Employee;
import com.herui.reggie_takeout.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
