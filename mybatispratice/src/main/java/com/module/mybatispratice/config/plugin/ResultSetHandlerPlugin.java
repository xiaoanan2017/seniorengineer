package com.module.mybatispratice.config.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author xiaoaa
 * @date 2022/8/12 22:14
 **/
@Intercepts({@Signature(
        type = ResultSetHandler.class,
        method = "handleResultSets",
        args = {Statement.class})
})
@Slf4j
@Component
public class ResultSetHandlerPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        PreparedStatement ps = (PreparedStatement)args[0];
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        ps.execute();
        log.info("ResultSetHandler 插件执行...");
        return resultSetHandler.handleResultSets(ps);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
