package com.example.domain.handler;

import com.alibaba.fastjson.JSON;
import com.example.domain.entity.Result;
import com.example.domain.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result result = Result.error("您无权访问该接口");
        WebUtils.renderString(response, JSON.toJSONString(result));
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

}
