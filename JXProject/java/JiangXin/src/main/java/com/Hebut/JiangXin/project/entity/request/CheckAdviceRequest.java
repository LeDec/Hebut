package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看教师教评意见请求实体")
public class CheckAdviceRequest {

    @ApiModelProperty(value = "验证请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "教师学工号")
    private String teacherId;
}
