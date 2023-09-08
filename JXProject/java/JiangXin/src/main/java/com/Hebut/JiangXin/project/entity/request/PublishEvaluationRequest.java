package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidong
 */
@Data
@Api(tags = "发布教评请求实体")
public class PublishEvaluationRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginning;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime deadline;

}
