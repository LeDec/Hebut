package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "朋友实体类")
public class FriendsResponse {

    @ApiModelProperty(value = "用户编号")
    private int user_id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String profile;

    @ApiModelProperty(value = "硬币数")
    private int coin;

    @ApiModelProperty(value = "成就数")
    private int achievement;
}
