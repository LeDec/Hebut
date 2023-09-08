package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "显示所有学院返回实体")
public class ShowAllInstituteResponse {

    @ApiModelProperty(value = "学院编号")
    private String instituteId;

    @ApiModelProperty(value = "学院名称")
    private String instituteName;


}
