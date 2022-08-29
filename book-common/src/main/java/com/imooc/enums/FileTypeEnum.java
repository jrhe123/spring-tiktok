package com.imooc.enums;

/**
 * @Desc: 文件类型 枚举
 */
public enum FileTypeEnum {
    BGIMG(1, "USER_BACKGROUND"),
    FACE(2, "USER_AVATAR");

    public final Integer type;
    public final String value;

    FileTypeEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
