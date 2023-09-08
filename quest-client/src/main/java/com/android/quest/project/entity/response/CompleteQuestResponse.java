package com.android.quest.project.entity.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "完成任务返回实体类")
public class CompleteQuestResponse {

    @ApiModelProperty(value = "是否产生成就")
    private boolean is_achievement;

    @ApiModelProperty(value = "成就编号")
    private String achievement_id;
}
