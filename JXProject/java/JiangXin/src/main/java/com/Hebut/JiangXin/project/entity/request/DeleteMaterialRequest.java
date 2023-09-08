package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@Api(tags = "删除资料请求实体")
public class DeleteMaterialRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "关系编号")
    private String projectMaterialId;
}
