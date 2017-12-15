
package com.mybatisplus.plugins.parser.tenant;

import net.sf.jsqlparser.expression.Expression;

/**
 * <p>
 * 租户处理器（ TenantId 行级 ）
 * </p>
 *
 * @author hubin
 * @since 2017-08-31
 */
public interface TenantHandler {

    Expression getTenantId();

    String getTenantIdColumn();

    boolean doTableFilter(String tableName);
}
