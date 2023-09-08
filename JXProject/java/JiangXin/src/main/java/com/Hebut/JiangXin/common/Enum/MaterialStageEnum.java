package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum MaterialStageEnum {
    /**
     * 中期报告
     */
    MEDIUM("1", "中期报告"),


    /**
     * 答辩报告
     */
    PRE("2", "答辩报告"),


    /**
     * 结项报告
     */
    END("3", "结项报告");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    MaterialStageEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
