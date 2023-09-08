package com.Hebut.JiangXin.project.entity.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看报名列表请求实体")

public class CheckAuditEnrollRequest {

    @ApiModelProperty(value = "查看请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次Id ")
    private String batchId;

}
