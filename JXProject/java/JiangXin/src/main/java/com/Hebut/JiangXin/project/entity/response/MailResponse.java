package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "发送邮件返回实体")
public class MailResponse {

    @ApiModelProperty(value = "用户学号")
    private String userId;

    @ApiModelProperty(value = "验证码")
    private String code;

}
