
package com.mybatisplus.incrementer;


/**
 * <p>
 * 表关键词 key 生成器接口
 * </p>
 *
 * @author hubin
 * @Date 2017-05-08
 */
public interface IKeyGenerator {

    /**
     * <p>
     * 执行 key 生成 SQL
     * </p>
     *
     * @param incrementerName 序列名称
     * @return
     */
    String executeSql(String incrementerName);

}
