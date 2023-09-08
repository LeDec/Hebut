package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看时间请求实体")
public class CheckTimeRequest {
    @ApiModelProperty(value = "查询验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;
}
