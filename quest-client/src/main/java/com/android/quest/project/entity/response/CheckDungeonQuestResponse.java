package com.android.quest.project.entity.response;

import com.android.quest.project.entity.request.AddDungeonQuestRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查看副本可选择任务实体类")
public class CheckDungeonQuestResponse {

    @ApiModelProperty(value = "数量")
    private int count;

    @ApiModelProperty(value = "任务列表")
    private List<DungeonQuestResponse> questList;
}
