package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "副本任务实体类")
public class DungeonQuestResponse {

    @ApiModelProperty(value = "任务编号")
    private int quest_id;

    @ApiModelProperty(value = "任务标题")
    private String title;

    @ApiModelProperty(value = "任务类型")
    private String questEnum;
}
