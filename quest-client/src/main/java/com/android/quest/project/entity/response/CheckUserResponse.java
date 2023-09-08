package com.android.quest.project.entity.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看用户返回实体类")
public class CheckUserResponse {


    @ApiModelProperty(value = "用户编号")
    private int userId;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "硬币")
    private int coin;
}
