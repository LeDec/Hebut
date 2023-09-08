package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "增加教评请求实体")
public class AddEvaluationRequest {

    @ApiModelProperty(value = "学生学工号")
    private String studentId;

    @ApiModelProperty(value = "老师学工号")
    private String teacherId;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "教评成绩1")
    private float gradeOne;

    @ApiModelProperty(value = "建议1")
    private String suggestionOne;

    @ApiModelProperty(value = "教评成绩2")
    private float gradeTwo;

    @ApiModelProperty(value = "建议2")
    private String suggestionTwo;

    @ApiModelProperty(value = "教评成绩3")
    private float gradeThree;

    @ApiModelProperty(value = "建议3")
    private String suggestionThree;

    @ApiModelProperty(value = "教评成绩4")
    private float gradeFour;

    @ApiModelProperty(value = "建议4")
    private String suggestionFour;

    @ApiModelProperty(value = "建议")
    private String suggestion;
}
