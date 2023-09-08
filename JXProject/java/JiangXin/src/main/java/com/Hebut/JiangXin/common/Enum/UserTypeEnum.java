package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum UserTypeEnum {

    /**
     * 系统管理员
     */
    SYSTEM_ADMIN("0", "系统管理员"),

    /**
     * 管理员
     */
    ADMIN("1", "管理员"),

    /**
     * 班主任
     */
    CLASS_TEACHER("2", "班主任"),

    /**
     * 指导老师
     */
    TEACHER("3", "指导老师"),

    /**
     * 专家
     */
    EXPERT("4", "专家"),

    /**
     * 学生
     */
    STUDENT("5", "学生"),

    /**
     * 企业
     */
    ENTERPRISE("6", "企业");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    UserTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
