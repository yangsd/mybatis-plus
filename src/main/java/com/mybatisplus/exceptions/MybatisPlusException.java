
package com.mybatisplus.exceptions;

/**
 * <p>
 * MybatisPlus 异常类
 * </p>
 *
 * @author hubin
 * @Date 2016-01-23
 */
public class MybatisPlusException extends RuntimeException {

    public MybatisPlusException(String message) {
        super(message);
    }

    public MybatisPlusException(Throwable throwable) {
        super(throwable);
    }

    public MybatisPlusException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
