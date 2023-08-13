package com.herui.reggie_takeout;

import com.herui.reggie_takeout.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReggieTakeoutApplicationTests {

    @Test
    void contextLoads() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9." +
                "eyJuYW1lIjoi566h55CG5ZGYIiwiaWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJleHAiOjE2OTE0MTg5Nzl9." +
                "Tdwn5qKnjLZqoL5OCw7fcctlNRyNbM1LOt5MdZYixVw";
        Claims claims = JwtUtil.parseJWT(jwt);
        System.out.println(claims);
    }

}
