package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "增加项目专业关系请求实体")
public class AddProjectMajorRelationRequest {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "专业编号")
    private String majorId;
}
