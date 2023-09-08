package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看活动学生列表请求实体")
public class CheckActivityStudentRequest {

    @ApiModelProperty(value = "查询请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "活动编号")
    private String activityId;
}
