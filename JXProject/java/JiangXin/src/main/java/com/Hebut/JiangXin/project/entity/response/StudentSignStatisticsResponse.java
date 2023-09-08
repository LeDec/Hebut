package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "统计学生签到返回实体")
public class StudentSignStatisticsResponse {

    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "学生姓名")
    private String userName;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "签到次数")
    private int times;

}
