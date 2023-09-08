package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "显示所有专业请求实体")
@Data
public class ShowAllMajorRequest {

    @ApiModelProperty(value = "查询验证")
    private CheckRequest checkRequest;
}
