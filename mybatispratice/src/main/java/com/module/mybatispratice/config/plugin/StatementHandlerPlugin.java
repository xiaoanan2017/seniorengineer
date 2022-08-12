package com.module.mybatispratice.config.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Properties;

/**
 * @author xiaoaa
 * @date 2022/8/12 22:14
 **/
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "query",
        args = {Statement.class, ResultHandler.class})
})
@Slf4j
@Component
public class StatementHandlerPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Statement statement = (Statement)args[0];
        ResultHandler resultHandler = (ResultHandler)args[1];
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        log.info("StatementHandlerPlugin 插件执行...");
        return statementHandler.query(statement, resultHandler);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
