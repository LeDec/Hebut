package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@Api(tags = "下载项目资料")
public class DownloadMaterialRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "资料编号")
    private String materialId;
}
