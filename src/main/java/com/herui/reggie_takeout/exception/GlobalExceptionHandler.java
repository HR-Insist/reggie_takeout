package com.herui.reggie_takeout.exception;

import com.herui.reggie_takeout.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice  // 异常处理注解 等价于：@ControllerAdvice+@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)  // 捕获SQL异常
    public Result ex(SQLException exception){
        exception.printStackTrace();
        return Result.error("对不起，操作失败，请联系管理员!");
    }

    @ExceptionHandler(CustomException.class)  // 捕获顾客操作异常
    public Result ex(CustomException exception){
        log.info(exception.getMessage());
        return Result.error(exception.getMessage());
    }


}
