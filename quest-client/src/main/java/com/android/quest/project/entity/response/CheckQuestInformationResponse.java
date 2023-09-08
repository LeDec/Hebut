package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看任务信息返回实体类")
public class CheckQuestInformationResponse {

    @ApiModelProperty(value = "任务编号")
    private int quest_id;

    @ApiModelProperty(value = "任务标题")
    private String title;

    @ApiModelProperty(value = "任务类型")
    private String questEnum;

    @ApiModelProperty(value = "硬币数")
    private int coin;

    @ApiModelProperty(value = "自定义奖励")
    private String self_treasure;

    @ApiModelProperty(value = "连击")
    private int combo;

    @ApiModelProperty(value = "是否完成")
    private String is_complete;
}
