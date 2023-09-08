package com.Hebut.JiangXin.project.entity.request;

/**
 * @author lidong
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "显示所有可报名项目请求实体")
@Data
public class ShowAllApplicationRequest {

    @ApiModelProperty(value = "查询验证")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "批次编号")
    private String batchId;
}
