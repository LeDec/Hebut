package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查看该学院专业请求实体")
@Data
public class ShowMajorRequest {
    @ApiModelProperty(value = "专业编号")
    private String instituteId;
}
