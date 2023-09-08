package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "搜索审核报名表请求实体")
@Data
public class SearchAuditEnrollRequest {

    @ApiModelProperty(value = "查看请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "报名人姓名")
    private String applicantName;

    @ApiModelProperty(value = "报名人学号")
    private String applicantUserId;

}
