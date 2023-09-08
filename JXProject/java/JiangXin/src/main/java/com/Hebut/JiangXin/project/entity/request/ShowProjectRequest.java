package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查看所有项目请求实体")
@Data
public class ShowProjectRequest {

    @ApiModelProperty(value = "分页")
    private PageRequest pageRequest;

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "批次编号")
    private String batchId;
}
