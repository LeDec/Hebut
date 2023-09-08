package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "成就返回类")
public class GetAchievementResponse {

    @ApiModelProperty(value = "成就列表")
    private List<AchievementResponse> achievementResponseList;

    @ApiModelProperty(value = "个数")
    private int count;
}
