package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@Api(tags = "验证")
public class CommonRequest {

    @ApiModelProperty(value = "用户学号")
    private String userId;

    @ApiModelProperty(value = "token")
    private String token;

}
