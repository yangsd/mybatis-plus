
package com.mybatisplus.mapper;


import com.mybatisplus.core.MybatisAbstractSQL;
import com.mybatisplus.util.StringUtils;

/**
 * <p>
 * 实现AbstractSQL ，实现WHERE条件自定义
 * </p>
 *
 * @author yanghu , Caratacus , hubin
 * @Date 2016-08-22
 */
@SuppressWarnings("serial")
public class SqlPlus extends MybatisAbstractSQL<SqlPlus> {

    private final String IS_NOT_NULL = " IS NOT NULL";
    private final String IS_NULL = " IS NULL";

    @Override
    public SqlPlus getSelf() {
        return this;
    }

    /**
     * <p>
     * IS NOT NULL查询
     * </p>
     *
     * @param columns 以逗号分隔的字段名称
     * @return this
     */
    public SqlPlus IS_NOT_NULL(String columns) {
        handerNull(columns, IS_NOT_NULL);
        return this;
    }

    /**
     * <p>
     * IS NULL查询
     * </p>
     *
     * @param columns 以逗号分隔的字段名称
     * @return
     */
    public SqlPlus IS_NULL(String columns) {
        handerNull(columns, IS_NULL);
        return this;
    }

    /**
     * <p>
     * 将EXISTS语句添加到WHERE条件中
     * </p>
     *
     * @param value
     * @return
     */
    public SqlPlus EXISTS(String value) {
        handerExists(value, false);
        return this;
    }

    /**
     * <p>
     * 处理EXISTS操作
     * </p>
     *
     * @param value
     * @param isNot 是否为NOT EXISTS操作
     */
    private void handerExists(String value, boolean isNot) {
        if (StringUtils.isNotEmpty(value)) {
            StringBuilder inSql = new StringBuilder();
            if (isNot) {
                inSql.append(" NOT");
            }
            inSql.append(" EXISTS (").append(value).append(")");
            WHERE(inSql.toString());
        }
    }

    /**
     * <p>
     * 将NOT_EXISTS语句添加到WHERE条件中
     * </p>
     *
     * @param value
     * @return
     */
    public SqlPlus NOT_EXISTS(String value) {
        handerExists(value, true);
        return this;
    }

    /**
     * <p>
     * 以相同的方式处理null和notnull
     * </p>
     *
     * @param columns 以逗号分隔的字段名称
     * @param sqlPart SQL部分
     */
    private void handerNull(String columns, String sqlPart) {
        if (StringUtils.isNotEmpty(columns)) {
            String[] cols = columns.split(",");
            for (String col : cols) {
                if (StringUtils.isNotEmpty(col.trim())) {
                    WHERE(col + sqlPart);
                }
            }
        }
    }
}
