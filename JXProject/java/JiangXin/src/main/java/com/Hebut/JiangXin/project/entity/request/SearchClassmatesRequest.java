package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author lidong
 */
@ApiModel(value = "搜索匠心班学生信息请求实体")
@Data
public class SearchClassmatesRequest {

    @ApiModelProperty(value = "查询请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentId;

    @ApiModelProperty(value = "学院")
    private String institute;

    @ApiModelProperty(value = "专业")
    private String major;
}
