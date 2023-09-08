package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum IsDelEnum {

    /**
     * 已删除
     */
    DELETE("1", "已删除"),

    /**
     * 未删除
     */
    UNDELETE("0", "未删除");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    IsDelEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
