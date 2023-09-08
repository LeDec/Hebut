package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查询成绩返回实体")
public class SearchScoreResponse {
    @ApiModelProperty(value = "平时成绩")
    private float usualScore;

    @ApiModelProperty(value = "答辩成绩")
    private float defenseScore;

    @ApiModelProperty(value = "活动成绩")
    private float activityScore;

    @ApiModelProperty(value = "总成绩")
    private float score;
}
