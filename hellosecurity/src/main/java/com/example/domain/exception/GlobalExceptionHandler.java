package com.example.domain.exception;

import com.example.domain.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public Result<String> bizExceptionResolve(BizException e) {

        log.info("出现异常");

        return Result.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionResolve(Exception e) {

        log.info("出现异常");

        return Result.error(e.getMessage());
    }

    /**
     * 排除掉security 2个异常
     * @param e
     */
    @ExceptionHandler(AuthenticationException.class)
    public void AuthenticationExceptionResolve(AuthenticationException e) {

        throw e;
    }


    @ExceptionHandler(AccessDeniedException.class)
    public void AccessDeniedExceptionResolve(AccessDeniedException  e) {

        throw e;
    }

}
