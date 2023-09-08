package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查询报销返回实体")
public class CheckExpenseResponse {


    @ApiModelProperty(value = "报销编号")
    private String expenseId;

    @ApiModelProperty(value = "报销时间")
    private String expenseTime;

    @ApiModelProperty(value = "报销内容")
    private String expenseIntroduction;

    @ApiModelProperty(value = "报销凭证")
    private String certificate;

    @ApiModelProperty(value = "报销金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "报销状态")
    private String expenseStage;

    @ApiModelProperty(value = "是否通过")
    private String expenseIsPass;

    public static Comparator<CheckExpenseResponse> expensePass = new Comparator<CheckExpenseResponse>() {

        @Override
        public int compare(CheckExpenseResponse s1, CheckExpenseResponse s2) {
            return Integer.parseInt(s2.getExpenseIsPass()) - Integer.parseInt(s1.getExpenseIsPass());
        }
    };
}
