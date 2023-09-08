package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查询自己教评请求实体")
public class CheckOwnEvaluationRequest {

    @ApiModelProperty(value = "验证请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次ID")
    private String batchId;

}
