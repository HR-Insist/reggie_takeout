package com.herui.reggie_takeout.utils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public class UserIdUtil {

    public static Long UserId(HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT(token);
        return ((Integer)claims.get("id")).longValue();
    }

}
