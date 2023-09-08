package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@Api(tags = "当前批次请求实体")
public class NowBatchRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;
}
