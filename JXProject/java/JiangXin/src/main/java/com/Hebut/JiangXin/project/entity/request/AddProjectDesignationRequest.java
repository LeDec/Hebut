package com.Hebut.JiangXin.project.entity.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "增加专业请求实体")
public class AddProjectDesignationRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "项目Id列表")
    private String projectId;

    @ApiModelProperty(value = "项目编号列表")
    private String designation;
}
