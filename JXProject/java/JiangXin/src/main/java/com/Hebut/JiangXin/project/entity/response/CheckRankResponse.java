package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Comparator;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看教评排名返回实体")
public class CheckRankResponse {

    @ApiModelProperty(value = "老师学工号")
    private String teacherId;

    @ApiModelProperty(value = "老师姓名")
    private String teacherName;


    @ApiModelProperty(value = "成绩一")
    private float scoreOne;

    @ApiModelProperty(value = "成绩二")
    private float scoreTwo;

    @ApiModelProperty(value = "成绩三")
    private float scoreThree;

    @ApiModelProperty(value = "成绩四")
    private float scoreFour;

    @ApiModelProperty(value = "平均成绩")
    private String totalScore;

    public static Comparator<CheckRankResponse> TotalScore = new Comparator<CheckRankResponse>() {

        @Override
        public int compare(CheckRankResponse s1, CheckRankResponse s2) {

            float score1 = Float.parseFloat(s1.getTotalScore());
            float score2 = Float.parseFloat(s2.getTotalScore());
            return (int) (score2 - score1);
        }
    };
}

