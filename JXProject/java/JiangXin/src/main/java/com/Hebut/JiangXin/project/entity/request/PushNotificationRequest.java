package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@Api(tags = "推送项目请求实体")
public class PushNotificationRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "项目编号")
    private List<String> projectIdList;

}
