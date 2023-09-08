package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum BatchStageEnum {
    /**
     * 征集阶段
     */
    NONE("0", "匠心班未开启"),

    /**
     * 征集阶段
     */
    COLLECTING("1", "征集阶段"),

    /**
     * 进行阶段
     */
    ENROLL("2", "报名阶段"),

    /**
     * 结束阶段
     */
    MEDIUM("3", "项目进行中期阶段"),

    /**
     * 结束阶段
     */
    DEFENSE("4", "项目进行答辩阶段"),

    /**
     * 结束阶段
     */
    FINAL("5", "结束阶段");


    @Getter
    private final String code;

    @Getter
    private final String msg;

    BatchStageEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
