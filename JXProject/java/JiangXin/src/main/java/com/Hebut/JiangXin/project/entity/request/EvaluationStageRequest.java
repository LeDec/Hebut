package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "教评状态请求实体")
public class EvaluationStageRequest {

    @ApiModelProperty(value = "验证")
    private CheckRequest checkRequest;

}
