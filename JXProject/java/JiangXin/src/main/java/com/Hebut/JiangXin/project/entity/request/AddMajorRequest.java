package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "增加专业请求实体")
public class AddMajorRequest {
    @ApiModelProperty(value = "专业编号")
    private String majorId;

    @ApiModelProperty(value = "专业名称")
    private String majorName;
}
