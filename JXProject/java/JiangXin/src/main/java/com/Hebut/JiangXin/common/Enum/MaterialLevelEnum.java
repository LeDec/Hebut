package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum MaterialLevelEnum {
    /**
     * 中期报告
     */
    AUDIT("1", "待审核"),


    /**
     * 答辩报告
     */
    PASS("2", "已通过"),


    /**
     * 结项报告
     */
    REFUSE("3", "未通过");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    MaterialLevelEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
