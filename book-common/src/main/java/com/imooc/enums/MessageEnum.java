package com.imooc.enums;

/**
 * @Desc: 消息类型
 */
public enum MessageEnum {
    FOLLOW_YOU(1, "FOLLOW_YOU"),
    LIKE_VLOG(2, "LIKE_VLOG"),
    COMMENT_VLOG(3, "COMMENT_VLOG"),
    REPLY_YOU(4, "REPLY_YOU"),
    LIKE_COMMENT(5, "LIKE_COMMENT");

    public final Integer type;
    public final String value;

    MessageEnum(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
