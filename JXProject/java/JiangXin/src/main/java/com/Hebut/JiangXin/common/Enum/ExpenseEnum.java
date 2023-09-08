package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * 报销关系枚举
 *
 * @author lidong
 */
public enum ExpenseEnum {

    /**
     * 待审核
     */
    WAIT_AUDIT("0", "待审核"),

    /**
     * 审核中
     */
    AUDITING("1", "审核中"),

    /**
     * 未通过
     */
    REFUSE("2", "未通过"),

    /**
     * 已通过
     */
    PASSING("3", "已通过");


    @Getter
    private final String code;

    @Getter
    private final String msg;

    ExpenseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
