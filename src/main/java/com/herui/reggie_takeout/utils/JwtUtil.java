package com.herui.reggie_takeout.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String signKey = "cmVnZ2llX3Rha2VvdXRfdG9rZW5faGVydWlfand0X3NlY3JldGtleQ==";
    private static final Long expire = 86400000L;

    /**
     * 生成自定义 Key
     * @return 密钥
     */
    private static SecretKey generateKeyByDecoders() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(signKey));
    }

    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return jwt令牌
     */
    public static String generateJwt(Map<String, Object> claims){

        return Jwts.builder()
                .addClaims(claims)
                .signWith(generateKeyByDecoders())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public static Claims parseJWT(String jwt){
        return Jwts.parserBuilder()
                .setSigningKey(generateKeyByDecoders())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
