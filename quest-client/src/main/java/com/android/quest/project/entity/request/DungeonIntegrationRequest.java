package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "集成副本请求实体类")
public class DungeonIntegrationRequest {

    @ApiModelProperty(value = "管理员编号")
    private int adminId;

    @ApiModelProperty(value = "副本标题")
    private String title;

    @ApiModelProperty(value = "硬币数")
    private int coin;

    @ApiModelProperty(value = "任务编号")
    private List<Integer> questIdList;
}
