package com.Hebut.JiangXin.common.Enum;

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
     * 过期错误
     */
    OVERDUE_ERROR(3, "申请已过期"),
    /**
     * 注册错误——密码不一致
     */
    PASSWORD_REPEAT(4, "两次密码不一致"),
    /**
     * 注册错误——信息不全
     */
    INFORMATION_EMPTY(5, "信息不全"),
    /**
     * 注册错误——账号已存在
     */
    ID_EQUAL(6, "学号已注册"),
    /**
     * 验证错误
     */
    TOKEN_WRONG(7, "用户不存在，或账号在异地登录"),
    /**
     * 未找到错误
     */
    NOT_FOUND_ERROR(8, "进行的内容不存在"),
    /**
     * 验证错误
     */
    PAGE_WRONG(9, "请输入正确的页码"),
    /**
     * 数据库错误
     */
    DATABASE_WRONG(10, "请输入正确的页码"),
    /**
     * 文件删除失败
     */
    FILE_DELETE_ERROR(11, "文件删除失败"),
    /**
     * 关系不匹配
     */
    MISMATCH_ERROR(12, "关系不匹配"),
    /**
     * 数字溢出错误
     */
    NUMBER_OVERFLOW(13, "数字溢出错误"),
    /**
     * 重复操作错误
     */
    REPEAT_OPERATION(14, "重复操作错误"),
    /**
     * 时间逻辑错误
     */
    TIME_WRONG(15, "时间逻辑错误");
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
