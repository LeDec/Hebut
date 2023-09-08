package com.android.quest.project.entity.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "增加副本可选任务请求实体类")
public class AddDungeonQuestRequest {

    @ApiModelProperty(value = "管理员编号")
    private int adminId;

    @ApiModelProperty(value = "任务标题")
    private String title;

    @ApiModelProperty(value = "任务类型")
    private String questEnum;

    @ApiModelProperty(value = "硬币数")
    private int coin;
}
