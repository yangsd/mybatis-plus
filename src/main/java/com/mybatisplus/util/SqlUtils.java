
package com.mybatisplus.util;


import com.mybatisplus.enums.SqlLike;
import com.mybatisplus.exceptions.MybatisPlusException;
import com.mybatisplus.plugins.pagination.Pagination;
import com.mybatisplus.plugins.parser.ISqlParser;
import com.mybatisplus.plugins.parser.SqlInfo;

/**
 * <p>
 * SqlUtils工具类
 * </p>
 *
 * @author Caratacus
 * @Date 2016-11-13
 */
public class SqlUtils {

    private final static SqlFormatter sqlFormatter = new SqlFormatter();
    public final static String SQL_BASE_COUNT = "SELECT COUNT(1) FROM ( %s ) TOTAL";
    public static ISqlParser COUNT_SQL_PARSER = null;
    private static Class<ISqlParser> DEFAULT_CLASS = null;

    static {
        try {
            DEFAULT_CLASS = (Class<ISqlParser>) Class.forName("com.baomidou.mybatisplus.plugins.pagination.optimize.JsqlParserCountOptimize");
        } catch (ClassNotFoundException e) {
            //skip
        }
    }
    /**
     * <p>
     * 获取CountOptimize
     * </p>
     *
     * @param sqlParser   Count SQL 解析类
     * @param originalSql 需要计算Count SQL
     * @return SqlInfo
     */
    public static SqlInfo getCountOptimize(ISqlParser sqlParser, String originalSql) {
        // COUNT SQL 解析器
        if (null == COUNT_SQL_PARSER) {
            if (null != sqlParser) {
                // 用户自定义 COUNT SQL 解析
                COUNT_SQL_PARSER = sqlParser;
            } else {
                // 默认 JsqlParser 优化 COUNT
                try {
                    // TODO: 2017/11/20 这里我改动了下.
                    COUNT_SQL_PARSER =  DEFAULT_CLASS.newInstance();
                } catch (Exception e) {
                    throw new MybatisPlusException(e);
                }
            }
        }
        return COUNT_SQL_PARSER.optimizeSql(null, originalSql);
    }

    /**
     * 查询SQL拼接Order By
     *
     * @param originalSql 需要拼接的SQL
     * @param page        page对象
     * @param orderBy     是否需要拼接Order By
     * @return
     */
    public static String concatOrderBy(String originalSql, Pagination page, boolean orderBy) {
        if (orderBy && StringUtils.isNotEmpty(page.getOrderByField()) && page.isOpenSort()) {
            StringBuilder buildSql = new StringBuilder(originalSql);
            buildSql.append(" ORDER BY ").append(page.getOrderByField());
            buildSql.append(page.isAsc() ? " ASC " : " DESC ");
            return buildSql.toString();
        }
        return originalSql;
    }

    /**
     * 格式sql
     *
     * @param boundSql
     * @param format
     * @return
     */
    public static String sqlFormat(String boundSql, boolean format) {
        if (format) {
            return sqlFormatter.format(boundSql);
        } else {
            return boundSql.replaceAll("[\\s]+", " ");
        }
    }

    /**
     * <p>
     * 用%连接like
     * </p>
     *
     * @param str 原字符串
     * @return
     */
    public static String concatLike(String str, SqlLike type) {
        StringBuilder builder = new StringBuilder(str.length() + 3);
        switch (type) {
            case LEFT:
                builder.append("%").append(str);
                break;
            case RIGHT:
                builder.append(str).append("%");
                break;
            case CUSTOM:
                builder.append(str);
                break;
            default:
                builder.append("%").append(str).append("%");
        }
        return builder.toString();
    }

}
