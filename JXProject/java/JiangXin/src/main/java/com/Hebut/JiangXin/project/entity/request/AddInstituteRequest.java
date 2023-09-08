package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "增加学院请求实体")
public class AddInstituteRequest {
    @ApiModelProperty(value = "学院编号")
    private String instituteId;

    @ApiModelProperty(value = "学院名称")
    private String instituteName;
}
