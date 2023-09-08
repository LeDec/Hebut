package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查询教评状态返回实体")
public class EvaluationStageResponse {

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "批次名称")
    private String batchName;

    @ApiModelProperty(value = "开始时间")
    private String beginning;

    @ApiModelProperty(value = "结束时间")
    private String deadline;

    @ApiModelProperty(value = "批次阶段")
    private String batchStage;

    @ApiModelProperty(value = "教评状态")
    private String stage;

    @ApiModelProperty(value = "批次时间（排序用）")
    private LocalDateTime selectTime;

    public static Comparator<EvaluationStageResponse> evaluationTime = new Comparator<EvaluationStageResponse>() {

        @Override
        public int compare(EvaluationStageResponse s1, EvaluationStageResponse s2) {
            if (s1.getSelectTime().isAfter(s2.getSelectTime())) {
                return -1;
            } else {
                return 1;
            }
        }
    };

}
