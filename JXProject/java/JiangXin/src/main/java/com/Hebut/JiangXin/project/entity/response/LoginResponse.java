package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2021/11/4
 *
 * @author lidong
 */
@Data
@ApiModel(value = "登录返回实体")
public class LoginResponse {
    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "用户类型")
    private String Type;

    @ApiModelProperty(value = "token")
    private String token;
}
