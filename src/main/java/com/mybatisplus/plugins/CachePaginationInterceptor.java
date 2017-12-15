
package com.mybatisplus.plugins;

import java.sql.Connection;
import java.util.Properties;

import com.mybatisplus.plugins.pagination.Pagination;
import com.mybatisplus.plugins.parser.ISqlParser;
import com.mybatisplus.plugins.parser.SqlInfo;
import com.mybatisplus.util.SqlUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * <p>
 * 缓存分页拦截器
 * </p>
 *
 * @author hubin
 * @Date 2016-01-23
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class CachePaginationInterceptor extends PaginationInterceptor implements Interceptor {

    /**
     * COUNT SQL 解析
     */
    private ISqlParser sqlParser;
    /**
     * 溢出总页数，设置第一页
     */
    private boolean overflowCurrent = false;

    /**
     * Physical Pagination Interceptor for all the queries with parameter
     * {@link RowBounds}
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object target = invocation.getTarget();
        if (target instanceof StatementHandler) {
            return super.intercept(invocation);
        } else {
            RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
            if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
                return invocation.proceed();
            }
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Executor executor = (Executor) invocation.getTarget();
            Connection connection = executor.getTransaction().getConnection();
            Object parameterObject = invocation.getArgs()[1];
            BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
            String originalSql = boundSql.getSql();
            if (rowBounds instanceof Pagination) {
                Pagination page = (Pagination) rowBounds;
                if (page.isSearchCount()) {
                    SqlInfo sqlInfo = SqlUtils.getCountOptimize(sqlParser, originalSql);
                    super.queryTotal(overflowCurrent, sqlInfo.getSql(), mappedStatement, boundSql, page, connection);
                    if (page.getTotal() <= 0) {
                        return invocation.proceed();
                    }
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {

    }

    @Override
    public CachePaginationInterceptor setDialectType(String dialectType) {
        return this;
    }

    public CachePaginationInterceptor setSqlParser(ISqlParser sqlParser) {
        this.sqlParser = sqlParser;
        return this;
    }

    @Override
    public CachePaginationInterceptor setOverflowCurrent(boolean overflowCurrent) {
        this.overflowCurrent = overflowCurrent;
        return this;
    }

}
