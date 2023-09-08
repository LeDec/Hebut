package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */
public enum IsReadEnum {
    /**
     * 已读
     */
    READ(1, "已读"),

    /**
     * 未读
     */
    UNREAD(0, "未读");

    @Getter
    private final int code;

    @Getter
    private final String msg;

    IsReadEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
