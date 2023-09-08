package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看自己教评返回实体")
public class CheckOwnEvaluationResponse {

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "学生学号")
    private String studentId;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "意见一")
    private String adviceOne;

    @ApiModelProperty(value = "分数一")
    private float scoreOne;

    @ApiModelProperty(value = "意见二")
    private String adviceTwo;

    @ApiModelProperty(value = "分数二")
    private float scoreTwo;

    @ApiModelProperty(value = "意见三")
    private String adviceThree;

    @ApiModelProperty(value = "分数三")
    private float scoreThree;

    @ApiModelProperty(value = "意见四")
    private String adviceFour;

    @ApiModelProperty(value = "分数四")
    private float scoreFour;

    @ApiModelProperty(value = "意见")
    private String adviceTotal;

    @ApiModelProperty(value = "分数")
    private float scoreTotal;

    @ApiModelProperty(value = "教评时间（排序用）")
    private LocalDateTime updateTime;

    public static Comparator<CheckOwnEvaluationResponse> evaluationTime = new Comparator<CheckOwnEvaluationResponse>() {

        @Override
        public int compare(CheckOwnEvaluationResponse s1, CheckOwnEvaluationResponse s2) {
            if (s1.getUpdateTime().isAfter(s2.getUpdateTime())) {
                return 1;
            } else {
                return -1;
            }
        }
    };
}
