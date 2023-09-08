package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum ApplicationEnum {

    /**
     * 已申请
     */
    WAIT("1", "已申请"),

    /**
     * 已通过
     */
    FIRST_PASS("2", "初试已通过"),

    /**
     * 复试已通过
     */
    RETEST_PASS("3", "复试已通过"),

    /**
     * 初试未通过
     */
    FIRST_REFUSE("4", "初试未通过"),

    /**
     * 复试未通过
     */
    RETEST_REFUSE("5", "复试未通过");


    @Getter
    private final String code;

    @Getter
    private final String msg;

    ApplicationEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
