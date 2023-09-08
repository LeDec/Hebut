package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查看我的项目请求实体")
@Data
public class SearchMyProjectRequest {

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "分页")
    private PageRequest pageRequest;

    @ApiModelProperty(value = "项目类型")
    private String projectType;
}
