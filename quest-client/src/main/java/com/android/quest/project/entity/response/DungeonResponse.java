package com.android.quest.project.entity.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "副本实体类")
public class DungeonResponse {

    @ApiModelProperty(value = "任务编号")
    private int dungeon_id;

    @ApiModelProperty(value = "任务标题")
    private String title;

    @ApiModelProperty(value = "硬币数")
    private int coin;

    @ApiModelProperty(value = "是否完成")
    private int is_complete;

    @ApiModelProperty(value = "副本任务列表")
    private List<DungeonQuestResponse> questList;

}
