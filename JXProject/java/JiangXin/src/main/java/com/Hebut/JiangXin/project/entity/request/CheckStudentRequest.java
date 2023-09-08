package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看学生请求实体")
public class CheckStudentRequest {

    @ApiModelProperty(value = "查询请求")
    private CheckRequest checkRequest;
}
