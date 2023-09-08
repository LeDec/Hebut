package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@Api(tags = "附件下载验证")
public class DownloadAttachmentRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "申请编号")
    private String relationId;
}
