package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看全部成绩返回实体")
public class SearchAllScoreResponse {

    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目编号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "平时成绩")
    private float usualScore;

    @ApiModelProperty(value = "答辩成绩")
    private float defenseScore;

    @ApiModelProperty(value = "其他成绩")
    private float activityScore;

    @ApiModelProperty(value = "总成绩")
    private float score;

    @ApiModelProperty(value = "签到次数")
    private int times;
}
