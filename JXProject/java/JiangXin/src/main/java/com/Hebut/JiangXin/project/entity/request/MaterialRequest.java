package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2022/1/13
 *
 * @author lidong
 */
@Data
public class MaterialRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "资料编号")
    private String materialId;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "当前阶段")
    private String stage;

    @ApiModelProperty(value = "资料类型")
    private String type;

}
