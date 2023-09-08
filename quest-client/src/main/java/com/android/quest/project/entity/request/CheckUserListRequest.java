package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看用户列表实体类")
public class CheckUserListRequest {


    @ApiModelProperty(value = "管理员编号")
    private int adminId;
}
