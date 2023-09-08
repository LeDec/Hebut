package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "审核项目请求实体")
public class AuditProjectRequest {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "是否通过")
    private boolean isPass;
}
