package com.android.quest.project.entity.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "好友返回实体类")
public class GetFriendsResponse {


    @ApiModelProperty(value = "好友列表")
    private List<FriendsResponse> friendsResponseList;

}
