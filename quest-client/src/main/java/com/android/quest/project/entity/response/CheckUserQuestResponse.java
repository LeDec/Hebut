package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查看用户任务返回实体类")
public class CheckUserQuestResponse {

    @ApiModelProperty(value = "总数")
    private int count;

    @ApiModelProperty(value = "完成数")
    private int over;

    @ApiModelProperty(value = "每日奖励是否领取")
    private String isDailyOver;

    @ApiModelProperty(value = "每周奖励是否领取")
    private String isWeeklyOver;

    @ApiModelProperty(value = "任务列表")
    private List<QuestResponse> questResponseList;
}
