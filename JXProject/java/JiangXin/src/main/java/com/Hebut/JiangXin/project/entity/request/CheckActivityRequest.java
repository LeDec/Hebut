package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看活动列表")
public class CheckActivityRequest {

    @ApiModelProperty(value = "验证请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次Id")
    private String batchId;
}
