package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * 用户项目关系枚举
 *
 * @author lidong
 */

public enum ProjectUserRelationEnum {

    /**
     * 发布者
     */
    APPLICANT("1", "发布者"),

    /**
     * 专家
     */
    EXPERT("2", "专家"),

    /**
     * 指导老师
     */
    TEACHER("3", "指导老师"),

    /**
     * 组员
     */
    PARTNER("4", "组员");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    ProjectUserRelationEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
