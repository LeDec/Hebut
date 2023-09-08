package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看副本实体类")
public class CheckDungeonResponse {

    @ApiModelProperty(value = "副本编号")
    private int dungeon_id;

    @ApiModelProperty(value = "副本标题")
    private String title;

    @ApiModelProperty(value = "硬币数")
    private int coin;

    @ApiModelProperty(value = "参与人数")
    private int userCount;

}
