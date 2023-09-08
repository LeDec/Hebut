package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查看项目内容请求实体")
@Data
public class ScanProjectRequest {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

}
