package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "制定任务请求实体类")
public class DevelopQuestRequest {

    @ApiModelProperty(value = "用户编号")
    private int user_id;

    @ApiModelProperty(value = "任务名称")
    private String title;

    @ApiModelProperty(value = "任务类型")
    private String type;

    @ApiModelProperty(value = "自定义奖励")
    private String self_treasure;

    @ApiModelProperty(value = "硬币数")
    private int coin;

}
