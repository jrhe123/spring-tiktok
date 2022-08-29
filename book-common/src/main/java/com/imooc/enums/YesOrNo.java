package com.imooc.enums;

/**
 * @Desc: 是否 枚举
 */
public enum YesOrNo {
    NO(0, "NO"),
    YES(1, "YES");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
