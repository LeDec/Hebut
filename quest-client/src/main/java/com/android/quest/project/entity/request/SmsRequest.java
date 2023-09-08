package com.android.quest.project.entity.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "发送验证码请求实体类")
public class SmsRequest {

    @ApiModelProperty(value = "手机号")
    private String phone;

}
