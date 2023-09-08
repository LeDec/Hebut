package com.android.quest.project.entity.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "完成任务请求实体类")
public class CompleteQuestRequest {

    @ApiModelProperty(value = "用户编号")
    private int user_id;

    @ApiModelProperty(value = "用户编号")
    private int quest_id;

}
