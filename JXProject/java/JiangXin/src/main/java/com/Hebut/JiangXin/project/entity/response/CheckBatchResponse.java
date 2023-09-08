package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查询批次返回实体")
public class CheckBatchResponse {

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "批次名称")
    private String batchName;

    @ApiModelProperty(value = "批次阶段")
    private String batchStage;

    @ApiModelProperty(value = "征集阶段开始时间")
    private LocalDateTime selectBeginning;

    @ApiModelProperty(value = "报名阶段开始时间")
    private LocalDateTime enrollBeginning;

    @ApiModelProperty(value = "中期阶段开始时间")
    private LocalDateTime mediumBeginning;

    @ApiModelProperty(value = "答辩阶段开始时间")
    private LocalDateTime defendBeginning;

    @ApiModelProperty(value = "征集阶段截止时间")
    private LocalDateTime selectDeadline;

    @ApiModelProperty(value = "报名阶段截止时间")
    private LocalDateTime enrollDeadline;

    @ApiModelProperty(value = "中期阶段开始时间")
    private LocalDateTime mediumDeadline;

    @ApiModelProperty(value = "答辩阶段开始时间")
    private LocalDateTime defendDeadline;

    @ApiModelProperty(value = "已报销金额")
    private BigDecimal amount;

    public static Comparator<CheckBatchResponse> batchTime = new Comparator<CheckBatchResponse>() {

        @Override
        public int compare(CheckBatchResponse s1, CheckBatchResponse s2) {
            if (s1.getSelectBeginning().isBefore(s2.getSelectBeginning())) {
                return 1;
            } else {
                return -1;
            }
        }
    };
}
