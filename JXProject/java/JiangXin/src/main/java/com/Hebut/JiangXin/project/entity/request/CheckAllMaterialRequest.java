package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看项目所有已提交资料请求实体")
public class CheckAllMaterialRequest {
    @ApiModelProperty(value = "查看请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "项目编号")
    private String projectId;
}
