package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2022/1/13
 *
 * @author lidong
 */
@Data
public class ModifyScoreRequestPromoteRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "学生学号")
    private String studentId;

    @ApiModelProperty(value = "成绩")
    private float score;
}
