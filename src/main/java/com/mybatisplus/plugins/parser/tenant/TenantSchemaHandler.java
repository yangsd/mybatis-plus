
package com.mybatisplus.plugins.parser.tenant;

/**
 * <p>
 * 租户处理器（ Schema 表级 ）
 * </p>
 *
 * @author hubin
 * @since 2017-08-31
 */
public interface TenantSchemaHandler {

    String getTenantSchema();

    boolean doTableFilter(String tableName);
}
