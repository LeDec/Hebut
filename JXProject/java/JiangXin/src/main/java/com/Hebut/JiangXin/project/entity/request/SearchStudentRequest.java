package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查询学生用户请求实体")
@Data
public class SearchStudentRequest {

    @ApiModelProperty(value = "查询请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "批次名称")
    private String batchName;

    @ApiModelProperty(value = "姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentId;

    @ApiModelProperty(value = "学院")
    private String institute;

    @ApiModelProperty(value = "专业")
    private String major;
}
