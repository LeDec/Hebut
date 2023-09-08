package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "修改教评时间请求实体")
public class ChangeEvaluationTimeRequest {
    @ApiModelProperty(value = "查询验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginning;

    @ApiModelProperty(value = "截止时间")
    private LocalDateTime deadline;
}
