package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查询用户报销金额请求实体")
public class InquireUserExpenseRequest {

    @ApiModelProperty(value = "登录验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "项目编号")
    private String projectId;
}
