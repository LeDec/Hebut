package com.android.quest.project.entity.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "更新任务实体类")
public class UpdateQuestRequest {

    @ApiModelProperty(value = "用户编号")
    private int user_id;
}
