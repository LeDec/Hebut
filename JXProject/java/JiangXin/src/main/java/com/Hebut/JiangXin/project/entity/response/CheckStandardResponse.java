package com.Hebut.JiangXin.project.entity.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看成绩占比返回实体")
public class CheckStandardResponse {

    @ApiModelProperty(value = "平时占比")
    private String usual;

    @ApiModelProperty(value = "答辩占比")
    private String defense;

    @ApiModelProperty(value = "其他占比")
    private String activity;
}
