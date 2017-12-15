
package com.mybatisplus.plugins.parser;

import org.apache.ibatis.reflection.MetaObject;

/**
 * <p>
 * SQL 解析过滤器
 * </p>
 *
 * @author hubin
 * @Date 2017-09-02
 */
public interface ISqlParserFilter {

    boolean doFilter(MetaObject metaObject);

}
