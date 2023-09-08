package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看报销审核返回实体")
public class ScanExpenseResponse {

    @ApiModelProperty(value = "报销编号")
    private String expenseId;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "报销时间")
    private String expenseTime;

    @ApiModelProperty(value = "报销内容")
    private String expenseIntroduction;

    @ApiModelProperty(value = "报销凭证")
    private String certificate;

    @ApiModelProperty(value = "报销金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "报销状态")
    private String stage;

    @ApiModelProperty(value = "报销状态编号")
    private String stageId;

    public static Comparator<ScanExpenseResponse> expensePass = new Comparator<ScanExpenseResponse>() {

        @Override
        public int compare(ScanExpenseResponse s1, ScanExpenseResponse s2) {
            return Integer.parseInt(s2.getStageId()) - Integer.parseInt(s1.getStageId());
        }
    };
}
