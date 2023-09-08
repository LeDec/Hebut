package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum ActivityEnum {
    /**
     * 已删除
     */
    ABSENCE("1", "未签到"),

    /**
     * 未删除
     */
    SIGN_ON("2", "已签到");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    ActivityEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
