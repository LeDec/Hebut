package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查看用户列表返回实体类")
public class CheckUserListResponse {

    @ApiModelProperty(value = "总数")
    private int count;

    @ApiModelProperty(value = "用户信息列表")
    private List<CheckUserResponse> checkUserResponseList;

}
