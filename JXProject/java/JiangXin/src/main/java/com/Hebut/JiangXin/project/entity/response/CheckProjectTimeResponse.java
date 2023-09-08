package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看项目时间返回实体")
public class CheckProjectTimeResponse {
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
