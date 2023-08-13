package com.herui.reggie_takeout.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@WebFilter(filterName = "CorsFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CorsFilter implements Filter {

    @Value("${allow.headers:X-Requested-With,Authorization,Content-Type, Token}")
    private String allowHeaders;

    @Value("${allow.origin:https://localhoss.com}")
    private String allowOrigin;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9528");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", allowHeaders);
//        response.setHeader("Access-Control-Allow-Headers", "*");
        log.info("CorsFilter {},{}", request.getRequestURL(), response.containsHeader("Access-Control-Allow-Origin"));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
