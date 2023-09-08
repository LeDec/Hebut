package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看专家未评分项目列表返回实体")
public class CheckProjectScoreResponse {

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "计划书")
    private String instruction;

    @ApiModelProperty(value = "项目来源")
    private String origin;

    @ApiModelProperty(value = "指导教师")
    private List<String> teachers;

    @ApiModelProperty(value = "成绩")
    private String score;
}
