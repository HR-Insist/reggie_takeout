package com.herui.reggie_takeout.exception;

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String msg){
        super(msg);
    }
}