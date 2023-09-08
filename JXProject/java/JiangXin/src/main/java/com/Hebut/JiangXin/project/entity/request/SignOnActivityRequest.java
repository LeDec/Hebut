package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lidong
 */
@ApiModel(value = "签到活动请求实体")
@Data
public class SignOnActivityRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "活动编号")
    private String activityId;

    @ApiModelProperty(value = "学生学号列表")
    private List<String> studentIdList;
}
