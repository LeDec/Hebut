package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum IsPromoteEnum {

    /**
     * 已删除
     */
    PROMOTE("1", "有特殊权限"),

    /**
     * 未删除
     */
    NOPE("0", "不拥有特殊权限");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    IsPromoteEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
