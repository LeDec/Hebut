package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看朋友请求实体类")
public class GetFriendsRequest {

    @ApiModelProperty(value = "用户编号")
    private int user_id;
}
