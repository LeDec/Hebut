package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看阶段时间返回实体")
public class CheckTimeResponse {

    @ApiModelProperty(value = "征集阶段开始时间")
    private String selectBeginning;

    @ApiModelProperty(value = "报名阶段开始时间")
    private String enrollBeginning;

    @ApiModelProperty(value = "中期阶段开始时间")
    private String mediumBeginning;

    @ApiModelProperty(value = "答辩阶段开始时间")
    private String defendBeginning;

    @ApiModelProperty(value = "征集阶段截止时间")
    private String selectDeadline;

    @ApiModelProperty(value = "报名阶段截止时间")
    private String enrollDeadline;

    @ApiModelProperty(value = "中期阶段开始时间")
    private String mediumDeadline;

    @ApiModelProperty(value = "答辩阶段开始时间")
    private String defendDeadline;


}
