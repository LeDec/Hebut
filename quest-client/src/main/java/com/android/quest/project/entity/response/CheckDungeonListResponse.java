package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查看副本列表实体类")
public class CheckDungeonListResponse {

    @ApiModelProperty(value = "副本数量")
    private int Count;

    @ApiModelProperty(value = "副本列表")
    private List<CheckDungeonResponse> checkDungeonRequestList;
}
