package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "文件返回实体")
public class fileResponse {

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "访问地址")
    private String location;
}
