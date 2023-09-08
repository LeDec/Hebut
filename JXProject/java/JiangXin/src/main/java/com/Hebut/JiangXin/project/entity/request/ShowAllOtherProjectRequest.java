package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "显示非匠心项目请求实体")
@Data
public class ShowAllOtherProjectRequest {

    @ApiModelProperty(value = "查询验证")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "项目阶段")
    private String projectStage;

}
