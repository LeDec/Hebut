package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * 成绩类型枚举
 *
 * @author lidong
 */

public enum ScoreTypeEnum {

    /**
     * 中期阶段
     */
    DEFENSE("1", "答辩成绩"),

    /**
     * 答辩阶段
     */
    USUAL("2", "平时成绩"),

    /**
     * 结项阶段
     */
    ACTIVITY("3", "活动成绩");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    ScoreTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
