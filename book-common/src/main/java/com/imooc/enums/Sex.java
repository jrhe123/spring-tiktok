package com.imooc.enums;

/**
 * @Desc: 性别 枚举
 */
public enum Sex {
    woman(0, "FEMALE"),
    man(1, "MALE"),
    secret(2, "UNISEX");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
