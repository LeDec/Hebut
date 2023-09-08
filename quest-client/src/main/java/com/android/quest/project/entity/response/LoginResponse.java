package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录返回实体类")
public class LoginResponse {

    @ApiModelProperty(value = "用户编号")
    private int userId;
}
