package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看往期评分项目列表请求实体")
public class CheckPastProjectScoreRequest {

    @ApiModelProperty(value = "验证请求")
    private CheckRequest checkRequest;


    @ApiModelProperty(value = "批次编号")
    private String batchId;


}
