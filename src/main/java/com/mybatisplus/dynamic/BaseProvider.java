
package com.mybatisplus.dynamic;

import com.mybatisplus.dynamic.entity.EntityColumn;
import com.mybatisplus.dynamic.mapperhelper.EntityHelper;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基础类
 *
 * @author liuzh
 */
public class BaseProvider {

    /**
     * 主键字段不能为空
     *
     * @param property
     * @param value
     */
    protected void notNullKeyProperty(String property, Object value) {
        if (value == null || (value instanceof String && isEmpty((String) value))) {
            throwNullKeyException(property);
        }
    }

    protected void throwNullKeyException(String property) {
        throw new NullPointerException("主键属性" + property + "不能为空!");
    }

    /**
     * 获取实体类型
     *
     * @param params
     * @return
     */
    protected Class<?> getEntityClass(Map<String, Object> params) {
        Class<?> entityClass = null;
        if (params.containsKey("record")) {
            entityClass = getEntity(params).getClass();
        } else if (params.containsKey("entityClass")) {
            entityClass = (Class<?>) params.get("entityClass");
        }
        if (entityClass == null) {
            throw new RuntimeException("无法获取实体类型!");
        }
        return entityClass;
    }

    /**
     * 获取实体类
     *
     * @param params
     * @return
     */
    protected Object getEntity(Map<String, Object> params) {
        Object result;
        if (params.containsKey("record")) {
            result = params.get("record");
        } else if (params.containsKey("key")) {
            result = params.get("key");
        } else {
            throw new RuntimeException("当前方法没有实体或主键参数!");
        }
        if (result == null) {
            throw new NullPointerException("实体或者主键参数不能为空!");
        }
        return result;
    }



    /**
     * 根据主键查询
     *
     * @param sql
     * @param metaObject
     * @param columns
     * @param suffix
     */
    protected void applyWherePk(SQL sql, MetaObject metaObject, Set<EntityColumn> columns, String suffix) {
        for (EntityColumn column : columns) {
            notNullKeyProperty(column.getProperty(), metaObject.getValue(column.getProperty()));
            sql.WHERE(column.getColumn() + "=#{" + (suffix != null ? suffix + "." : "") + column.getProperty() + "}");
        }
    }

    /**
     * Example条件
     */
    protected void applyOrderBy(SQL sql, MetaObject example, String defaultOrderByClause) {
        if (example == null) {
            return;
        }
        Object orderBy = example.getValue("orderByClause");
        if (orderBy != null) {
            sql.ORDER_BY((String) orderBy);
        } else if (defaultOrderByClause != null && defaultOrderByClause.length() > 0) {
            sql.ORDER_BY(defaultOrderByClause);
        }
    }

    protected boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    protected boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
}
