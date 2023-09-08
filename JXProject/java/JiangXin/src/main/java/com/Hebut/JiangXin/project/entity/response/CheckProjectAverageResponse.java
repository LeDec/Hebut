package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看项目评分均分列表评返回实体")
public class CheckProjectAverageResponse {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目阶段")
    private String projectStage;

    @ApiModelProperty(value = "审核阶段")
    private String audit;

    @ApiModelProperty(value = "计划书")
    private String instruction;

    @ApiModelProperty(value = "项目来源")
    private String origin;

    @ApiModelProperty(value = "指导教师")
    private List<String> teachers;

    @ApiModelProperty(value = "成绩")
    private float score;

    public static Comparator<CheckProjectAverageResponse> TotalScore = new Comparator<CheckProjectAverageResponse>() {

        @Override
        public int compare(CheckProjectAverageResponse s1, CheckProjectAverageResponse s2) {
            float score1 = s1.getScore();
            float score2 = s2.getScore();
            return (int) (score2 - score1);
        }
    };
}
