package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "显示所有已报名成员请求实体")
@Data
public class ShowAllApplicantRequest {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "验证请求")
    private CheckRequest checkRequest;


}
