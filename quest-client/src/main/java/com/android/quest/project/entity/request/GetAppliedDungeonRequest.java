package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "获取已参与副本任务请求实体类")
public class GetAppliedDungeonRequest {

    @ApiModelProperty(value = "用户编号")
    private int user_id;
}
