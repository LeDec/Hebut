package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@Api(tags = "下载项目计划书模板")
public class DownloadTemplateRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

}
