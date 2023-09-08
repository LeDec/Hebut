package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "验证码返回实体类")
public class SmsResponse {

    @ApiModelProperty(value = "验证码")
    private String verify_code;
}
