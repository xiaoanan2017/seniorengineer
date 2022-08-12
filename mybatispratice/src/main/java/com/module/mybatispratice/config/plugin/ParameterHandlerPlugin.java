package com.module.mybatispratice.config.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author xiaoaa
 * @date 2022/8/12 22:14
 **/
@Intercepts({@Signature(
        type = ParameterHandler.class,
        method = "setParameters",
        args = {PreparedStatement.class})
})
@Slf4j
@Component
public class ParameterHandlerPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        PreparedStatement ps = (PreparedStatement)args[0];
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        parameterHandler.setParameters(ps);
        log.info("ParameterHandlerPlugin 插件执行...");
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
