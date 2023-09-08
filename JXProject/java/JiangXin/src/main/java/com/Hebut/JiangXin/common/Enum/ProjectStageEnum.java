package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum ProjectStageEnum {

    /**
     * 报名阶段
     */
    SELECT("0", "报名阶段"),

    /**
     * 中期阶段
     */
    MEDIUM("1", "中期阶段"),

    /**
     * 答辩阶段
     */
    PRESENTATION("2", "答辩阶段"),

    /**
     * 结项阶段
     */
    ENDING("3", "结项阶段"),
    /**
     * 待审核
     */
    AUDIT("4", "待审核"),
    /**
     * 已审核通过
     */
    WAIT("5", "已审核通过"),
    /**
     * 审核未通过
     */
    PUSH("6", "已发布"),

    /**
     * 审核未通过
     */
    STOP("7", "审核未通过");
    @Getter
    private final String code;

    @Getter
    private final String msg;

    ProjectStageEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
