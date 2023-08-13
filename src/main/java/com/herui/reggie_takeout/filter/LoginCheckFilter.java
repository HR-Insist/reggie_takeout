package com.herui.reggie_takeout.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.herui.reggie_takeout.common.Result;
import com.herui.reggie_takeout.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@WebFilter(urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    // 路径匹配器，支持通配符
    private static AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (RequestMethod.OPTIONS.name().equals(request.getMethod())){
            log.info("OPTIONS请求");
            return;
        }
        // 获取本次请求的URL
        String url = request.getRequestURI();

        // 定义不需要拦截的请求路径
        String[] urls = {
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/employee/login/",
            "/employee/logout/",
            "/common/download"
        };

        // 检查路径是否不需要拦截
        boolean check = checkUrl(urls, url);

        // 如果不需要拦截，放行
        if (check){
            log.info("本次请求不需要拦截, {}",url);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 如果已经登录，放行
//        if(request.getSession().getAttribute("employee") != null){
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }
        String token = request.getHeader("token");
        if( !StringUtils.hasLength(token)){
            log.info("请求头的token为空，用户未登录");
            Result error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            servletResponse.getWriter().write(notLogin);
        }
        else {
            try {
                Claims claims = JwtUtil.parseJWT(token);
                log.info("令牌合法， {}", claims.get("name"));
                filterChain.doFilter(servletRequest,servletResponse);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("解析令牌失败，返回未登录错误信息!");
                Result error = Result.error("NOT_LOGIN");
                String notLogin = JSONObject.toJSONString(error);
                servletResponse.getWriter().write(notLogin);
            }
        }
        // 如果未登录，拦截，返回未登录的信息
//        log.info("用户未登录, {}",url);
//        servletResponse.getWriter().write(JSON.toJSONString(Result.error("NOT_LOGIN")));

    }

    /**
     * 路径匹配，检查本次请求是否放形
     * @param urls: 放行的url
     * @param requestUrl: 请求路径
     * @return 是否放行
     */
    public boolean checkUrl(String[] urls, String requestUrl){
        for (String url:urls){
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if(match){
                return true;
            }
        }
        return false;
    }
}
