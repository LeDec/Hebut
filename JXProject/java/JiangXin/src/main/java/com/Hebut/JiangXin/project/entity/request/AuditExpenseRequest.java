package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "审核初试报名请求实体")
public class AuditExpenseRequest {

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "报销编号")
    private String expenseId;

    @ApiModelProperty(value = "是否通过（1、通过；0、不通过）")
    private String isPass;
}
