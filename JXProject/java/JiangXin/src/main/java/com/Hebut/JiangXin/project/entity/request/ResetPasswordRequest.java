package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@ApiModel(value = "重置密码请求实体")
@Data
public class ResetPasswordRequest {

    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "验证码")
    private String code;

}
