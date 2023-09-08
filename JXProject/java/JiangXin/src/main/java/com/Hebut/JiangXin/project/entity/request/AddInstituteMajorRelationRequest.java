package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "增加学院专业关系请求实体")
public class AddInstituteMajorRelationRequest {

    @ApiModelProperty(value = "学院编号")
    private String instituteId;

    @ApiModelProperty(value = "专业编号")
    private String majorId;
}
