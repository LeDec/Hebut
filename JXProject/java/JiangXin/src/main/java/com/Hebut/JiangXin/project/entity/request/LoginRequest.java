package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2021/11/4
 *
 * @author lidong
 */
@Data
@ApiModel(value = "登陆实体类")
public class LoginRequest {

    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "密码")
    private String password;

}
