package com.herui.reggie_takeout.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;
    private Map<String, Object> map = new HashMap<>(); // 动态数据

    public static Result success(Integer code, String msg, Object data){
        return new Result(code, msg, data,null);
    }
    public static Result success(){
        return new Result(1, "successful", null,null);
    }

    public static Result success(String msg){
        return new Result(1, msg, null,null);
    }

    public static Result success(String msg, Object data){
        return new Result(1, msg, data,null);
    }

    public static Result success(Object data){
        return new Result(1, "successful", data,null);
    }

    public static Result error(String msg){
        return new Result(0, msg, null,null);
    }

    public static Result error(Integer code, String msg){
        return new Result(code, msg, null,null);
    }

    public Result add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
