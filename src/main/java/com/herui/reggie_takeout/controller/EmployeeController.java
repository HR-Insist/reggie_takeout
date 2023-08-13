package com.herui.reggie_takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.herui.reggie_takeout.common.Result;
import com.herui.reggie_takeout.pojo.Employee;
import com.herui.reggie_takeout.service.EmployeeService;
import com.herui.reggie_takeout.utils.JwtUtil;
import com.herui.reggie_takeout.utils.UserIdUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/login")
    public Result login(HttpServletRequest request, @RequestBody Employee employee){

        // 对密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 如果没有查询到，则返回登录失败结果
        if(emp == null){
            return Result.error("用户名不存在!");
        }

        //  查到结果，比对密码，密码错误
        if(!emp.getPassword().equals(password)){
            return Result.error("密码错误!");
        }

        // 密码正确，查看员工状态
        if (emp.getStatus() == 0){
            return Result.error("账号已禁用!");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", emp.getId());
        claims.put("username", emp.getUsername());
        claims.put("name", emp.getName());
        String jwt = JwtUtil.generateJwt(claims);
        return Result.success("login successfully!", jwt);

//        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result loginOut(){
        return Result.success("退出成功!");
    }

    @PostMapping
    public Result save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工, {}", employee);

        // 设置初始密码，并进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 获取登录人的ID
        employee.setCreateUser(UserIdUtil.UserId(request));
        employee.setUpdateUser(UserIdUtil.UserId(request));

        employeeService.save(employee);

        return Result.success();
    }

    /**
     * 员工信息分页查询
     * @param page: 页数
     * @param pageSize：每页大小
     * @param name：姓名
     * @return 查询结果
     */
    @GetMapping()
    public Result page(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam(required = false) String name){
        log.info("page={}, pageSize={}, name={}", page, pageSize, name);
        // 构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        lambdaQueryWrapper.like(name!=null, Employee::getName, name);
        // 添加排序条件
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        //  执行查询
        employeeService.page(pageInfo, lambdaQueryWrapper);

        return Result.success(pageInfo);
    }

    @GetMapping("/getLoginUser")
    public Result getLoginUser(HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT(token);
        return Result.success("获取登录用户成功", claims.get("username"));
    }

    @PatchMapping
    public Result update(HttpServletRequest request, @RequestBody Employee employee){

//        Claims claims = JwtUtil.parseJWT(token);
//        long updateUserid = ((Integer)claims.get("id")).longValue(); // 封装了工具类

//        employee.setUpdateTime(LocalDateTime.now());  // 被Mybatis-plus 自动填充功能实现类
        employee.setUpdateUser(UserIdUtil.UserId(request));
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功!");
    }
}
