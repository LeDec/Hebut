package com.android.quest.project.entity.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "修改头像请求实体类")
public class ChangeProfileRequest
{

    @ApiModelProperty(value = "用户编号")
    private int user_id;

    @ApiModelProperty(value = "头像")
    private String profile;
}
