package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "增加报销请求实体")
public class AddExpenseRequest {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "申请人编号")
    private String applicantId;

    @ApiModelProperty(value = "报销内容")
    private String expenseIntroduction;

    @ApiModelProperty(value = "报销金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "报销凭证")
    private String certificate;
}
