package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * 成绩类型枚举
 *
 * @author lidong
 */


public enum SaveEnum {

    /**
     * 中期阶段
     */
    SAVE("0", "暂存"),

    /**
     * 答辩阶段
     */
    SUBMIT("1", "提交");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    SaveEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
