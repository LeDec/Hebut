package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看项目评分均分列表请求实体")
public class CheckProjectAverageRequest {


    @ApiModelProperty(value = "验证请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

}
