package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看教评排名请求实体")
public class CheckRankRequest {

    @ApiModelProperty(value = "验证")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;
}
