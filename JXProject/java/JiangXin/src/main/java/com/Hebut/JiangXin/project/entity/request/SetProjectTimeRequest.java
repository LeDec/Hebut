package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidong
 */
@ApiModel(value = "设置项目时间请求实体")
@Data
public class SetProjectTimeRequest {

    @ApiModelProperty(value = "查询验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime enrollBeginning;

    @ApiModelProperty(value = "截止时间")
    private LocalDateTime enrollDeadline;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime mediumBeginning;

    @ApiModelProperty(value = "截止时间")
    private LocalDateTime mediumDeadline;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime defendBeginning;

    @ApiModelProperty(value = "截止时间")
    private LocalDateTime defendDeadline;
}
