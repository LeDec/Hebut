package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看提交资料状态请求实体")
public class CheckMaterialStageRequest {

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "资料阶段")
    private String materialStage;
}
