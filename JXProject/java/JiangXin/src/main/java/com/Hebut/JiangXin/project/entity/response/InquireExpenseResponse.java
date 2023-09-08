package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查询报销返回实体")
public class InquireExpenseResponse {

    @ApiModelProperty(value = "已报销金额")
    private BigDecimal expenseAmount;

    @ApiModelProperty(value = "剩余金额")
    private BigDecimal remainAmount;
}
