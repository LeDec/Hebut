package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "验证码请求实体")
public class KaptchaRequest {

    @ApiModelProperty(value = "验证码")
    private String code;
}
