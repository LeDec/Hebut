package com.android.quest.common.Enum;

import lombok.Getter;

public enum UserEnum {
    /**
     * 内部错误
     */
    ADMIN(0, "管理员"),
    /**
     * 无访问权限
     */
    TEACHER(1, "教师"),
    /**
     * 对象不存在
     */
    STUDENT(2, "学生");
    /**
     * 错误码
     */
    @Getter
    private final int code;
    /**
     * 错误信息
     */
    @Getter
    private final String msg;

    /**
     * 构造函数
     */
    UserEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
