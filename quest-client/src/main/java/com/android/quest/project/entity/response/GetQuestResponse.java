package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "获得任务实体返回类")
public class GetQuestResponse {


    @ApiModelProperty(value = "总数")
    private int count;

    @ApiModelProperty(value = "完成数")
    private int over;

    @ApiModelProperty(value = "任务列表")
    private List<QuestResponse> questResponseList;
}
