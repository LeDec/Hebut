package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "任务请求实体类")
public class GetQuestRequest {

    @ApiModelProperty(value = "用户编号")
    private String user_id;

    @ApiModelProperty(value = "任务类型")
    private String quest_type;
}
