
package com.mybatisplus.enums;

/**
 * <p>
 * 字段策略枚举类
 * </p>
 *
 * @author hubin
 * @Date 2016-09-09
 */
public enum FieldStrategy {
    IGNORED(0, "忽略判断"),
    NOT_NULL(1, "非 NULL 判断"),
    NOT_EMPTY(2, "非空判断");

    /**
     * 主键
     */
    private final int key;

    /**
     * 描述
     */
    private final String desc;

    FieldStrategy(final int key, final String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static FieldStrategy getFieldStrategy(int key) {
        FieldStrategy[] fss = FieldStrategy.values();
        for (FieldStrategy fs : fss) {
            if (fs.getKey() == key) {
                return fs;
            }
        }
        return FieldStrategy.NOT_NULL;
    }

    public int getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

}
