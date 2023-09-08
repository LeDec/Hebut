package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看教师教评意见返回实体")
public class CheckAdviceResponse {

    @ApiModelProperty(value = "教师姓名")
    private String teacherName;

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


}
