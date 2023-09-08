package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看用户任务情况实体类")
public class CheckUserQuestRequest {


    @ApiModelProperty(value = "管理员编号")
    private int adminId;

    @ApiModelProperty(value = "用户编号")
    private int userId;
}
