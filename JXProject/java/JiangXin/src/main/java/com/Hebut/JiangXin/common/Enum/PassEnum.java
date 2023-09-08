package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum PassEnum {
    /**
     * 通过
     */
    PASS("1", "通过"),

    /**
     * 不通过
     */
    REFUSE("0", "不通过");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    PassEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
