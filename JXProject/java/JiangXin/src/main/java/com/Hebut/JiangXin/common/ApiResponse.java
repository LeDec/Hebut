package com.Hebut.JiangXin.common;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用返回包装类
 *
 * @param <T> 泛型type，可传入任意类
 * @author lidong
 */
@Data
@ApiModel(value = "通用返回类")
public final class ApiResponse<T> {
    /**
     * 状态码
     * 0：成功
     * 1：失败
     * 其他状态码均代表失败
     */
    @ApiModelProperty(value = "返回响应码，0为成功，其他为失败")
    private int code;

    @ApiModelProperty(value = "返回标志位，true为成功，false为失败")
    private boolean success;

    /**
     * 返回信息
     * 通用返回信息为success或error
     */
    @ApiModelProperty(value = "返回信息")
    private String msg;

    /**
     * 返回数据
     * T为模板类
     */
    @ApiModelProperty(value = "返回数据")
    private T data;

    /**
     * 构造函数
     * 用于装配对象并返回
     *
     * @param code 响应码
     * @param msg  返回信息
     * @param data 返回数据
     */
    public ApiResponse(int code, boolean success, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    /**
     * 新建默认的成功对象和失败对象
     */
    private static final ApiResponse<Object> SUCCESS = new ApiResponse<>(0, true, "success", null);
    private static final ApiResponse<Object> ERROR = new ApiResponse<>(1, false, "error", null);

    /**
     * 失败返回方法
     *
     * @param code 响应码
     * @param msg  返回信息
     * @param data 返回数据
     * @return 返回对象
     */
    public static <T> ApiResponse<T> error(int code, String msg, T data) {
        return new ApiResponse<>(code, ERROR.isSuccess(), msg, data);
    }

    /**
     * 只返回错误信息，使用默认code和data
     *
     * @param msg 返回信息
     * @return 返回对象
     */
    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>(ERROR.getCode(), ERROR.isSuccess(), msg, null);
    }

    /**
     * 只返回错误信息和数据，使用默认code
     *
     * @param msg  返回信息
     * @param data 返回数据
     * @return 返回对象
     */
    public static <T> ApiResponse<T> error(String msg, T data) {
        return new ApiResponse<>(ERROR.getCode(), ERROR.isSuccess(), msg, data);
    }

    /**
     * 只返回code和msg
     *
     * @param code 响应码
     * @param msg  返回信息
     * @return 返回对象
     */
    public static <T> ApiResponse<T> error(int code, String msg) {
        return new ApiResponse<T>(code, ERROR.isSuccess(), msg, null);
    }

    /**
     * 根据错误枚举返回
     *
     * @param errorEnum 返回错误枚举
     * @return 返回对象
     */
    public static <T> ApiResponse<T> error(ErrorEnum errorEnum) {
        return new ApiResponse<>(errorEnum.getCode(), ERROR.isSuccess(), errorEnum.getMsg(), null);
    }

    /**
     * 成功返回方法
     *
     * @param code 响应码
     * @param msg  返回信息
     * @param data 返回数据
     * @return 返回对象
     */
    public static <T> ApiResponse<T> success(int code, String msg, T data) {
        return new ApiResponse<>(code, SUCCESS.isSuccess(), msg, data);
    }

    /**
     * 只返回数据，使用默认的code和msg
     *
     * @param data 返回数据
     * @return 返回对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS.getCode(), SUCCESS.isSuccess(), SUCCESS.getMsg(), data);
    }

    /**
     * 只返回信息和数据，使用默认的code
     *
     * @param msg  返回信息
     * @param data 返回数据
     * @return 返回对象
     */
    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(SUCCESS.getCode(), SUCCESS.isSuccess(), msg, data);
    }

    /**
     * 只返回提示信息，使用默认的code和data
     * 风险提示：在重载{@link ApiResponse#success(T data)}方法时，不可以传入字符串
     *
     * @param msg 返回信息
     * @return 返回对象
     */
    public static <T> ApiResponse<T> success(String msg) {
        return new ApiResponse<T>(SUCCESS.getCode(), SUCCESS.isSuccess(), msg, null);
    }

    /**
     * 成功的返回值
     *
     * @return 默认返回
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(SUCCESS.getCode(), SUCCESS.isSuccess(), SUCCESS.getMsg(), null);
    }
}
