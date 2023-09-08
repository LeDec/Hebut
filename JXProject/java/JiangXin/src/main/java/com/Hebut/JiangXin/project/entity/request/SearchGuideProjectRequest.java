package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查询我指导的项目请求实体")
@Data
public class SearchGuideProjectRequest {

    @ApiModelProperty(value = "查询验证")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "教师工号")
    private String teacherId;
}
