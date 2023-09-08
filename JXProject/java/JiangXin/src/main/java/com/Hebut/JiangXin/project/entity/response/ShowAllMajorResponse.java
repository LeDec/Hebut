package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "显示所有专业返回实体")
public class ShowAllMajorResponse {

    @ApiModelProperty(value = "专业编号")
    private String majorId;

    @ApiModelProperty(value = "专业名称")
    private String majorName;
}
