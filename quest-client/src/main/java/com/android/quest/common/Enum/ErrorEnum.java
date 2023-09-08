package com.android.quest.common.Enum;

import lombok.Getter;

/**
 * 通用错误枚举
 *
 * @author lidong
 */
public enum ErrorEnum {
    /**
     * 内部错误
     */
    INNER_ERROR(2, "系统发生异常，请联系管理员"),
    /**
     * 无访问权限
     */
    INVALID_ACCESS(401, "无访问权限"),
    /**
     * 登录错误
     */
    LOGIN_ERROR(1, "登录错误"),
    /**
     * 输入为空错误
     */
    INPUT_NULL(2, "输入为空"),
    /**
     * 对象不存在
     */
    NOT_FOUND_OBJECT(3, "对象不存在"),
    /**
     * 对象不存在
     */
    EXISTED_OBJECT(4, "对象已存在"),
    /**
     * 权限错误
     */
    TOKEN_WRONG(5, "权限错误"),

    /**
     * 删除错误
     */
    DELETE_WRONG(6, "删除错误"),
    /**
     * 删除错误
     */
    VERIFIED_WRONG(7, "验证码错误"),
    /**
     * 删除错误
     */
    COMPLETED(8, "已经完成");
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
    ErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
