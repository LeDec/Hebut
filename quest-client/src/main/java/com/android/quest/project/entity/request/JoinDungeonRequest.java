package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "参与副本请求实体类")
public class JoinDungeonRequest {
    @ApiModelProperty(value = "用户编号")
    private int user_id;

    @ApiModelProperty(value = "副本编号")
    private int dungeon_id;
}
