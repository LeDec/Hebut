package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum ApplicationIsPassEnum {


    /**
     * 已申请
     */
    WAIT("0", "已申请"),

    /**
     * 已通过
     */
    PASS("1", "初试已通过"),

    /**
     * 未通过
     */
    REFUSE("2", "未通过");


    @Getter
    private final String code;

    @Getter
    private final String msg;

    ApplicationIsPassEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
