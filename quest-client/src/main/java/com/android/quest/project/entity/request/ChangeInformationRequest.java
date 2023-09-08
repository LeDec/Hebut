package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "修改个人信息请求实体类")
public class ChangeInformationRequest {

    @ApiModelProperty(value = "用户编号")
    private int user_id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "昵称")
    private String profile;
}
