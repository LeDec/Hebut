package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */
public enum CommonEnum {

    /**
     * 成绩上限
     */
    SCORE_LIMIT(100, "成绩上限");


    @Getter
    private final int code;

    @Getter
    private final String msg;

    CommonEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
