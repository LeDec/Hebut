package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "成就实体类")
public class AchievementResponse {

    @ApiModelProperty(value = "成就编号")
    private String achievement_id;

    @ApiModelProperty(value = "成就类型")
    private String type;

    @ApiModelProperty(value = "任务标题")
    private String title;



}
