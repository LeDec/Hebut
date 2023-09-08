package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "设置分数占比请求实体")
@Data
public class SetStandardRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "平时成绩占比")
    private float usual;

    @ApiModelProperty(value = "活动成绩占比")
    private float activity;

    @ApiModelProperty(value = "答辩成绩占比")
    private float defense;

}
