package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "获得副本实体返回类")
public class GetDungeonResponse {

    @ApiModelProperty(value = "副本列表")
    private List<DungeonResponse> dungeonResponseList;
}
