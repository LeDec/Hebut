package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "收获全部任务完成实体类")
public class CollectTotalRequest {

    @ApiModelProperty(value = "用户编号")
    private int user_id;

    @ApiModelProperty(value = "类型编号")
    private String quest_type;
}
