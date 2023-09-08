package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看任务信息请求实体类")
public class CheckQuestInformationRequest {

    @ApiModelProperty(value = "用户编号")
    private int user_id;

    @ApiModelProperty(value = "用户编号")
    private int quest_id;
}
