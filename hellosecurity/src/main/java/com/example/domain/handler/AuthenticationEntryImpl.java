package com.example.domain.handler;

import com.alibaba.fastjson.JSON;
import com.example.domain.entity.Result;
import com.example.domain.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败出现的异常处理
 * @author xiaoaa
 * @date 2023/6/7 21:59
 **/
@Component
public class AuthenticationEntryImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        Result result = Result.error("用户认证失败请查询登录");
        WebUtils.renderString(response, JSON.toJSONString(result));
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}
