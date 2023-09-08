package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查看通知内容请求实体")
@Data
public class ScanInformRequest {

    @ApiModelProperty(value = "通知编号")
    private String informId;

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;
}
