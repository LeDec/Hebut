package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "显示各项目答辩成绩返回实体")
public class ShowAllDefenseScoreResponse {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "答辩成绩")
    private String score;

    @ApiModelProperty(value = "项目来源")
    private String origin;

    @ApiModelProperty(value = "指导教师")
    private List<String> teachers;
}
