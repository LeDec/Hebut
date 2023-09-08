package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "忘记密码请求实体类")
public class ForgetPasswordRequest {

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "新密码")
    private String new_password;
}
