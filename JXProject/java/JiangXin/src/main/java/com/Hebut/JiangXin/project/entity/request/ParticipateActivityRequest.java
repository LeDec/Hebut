package com.Hebut.JiangXin.project.entity.request;

/**
 * @author admin
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "学生参加活动请求实体")
@Data
public class ParticipateActivityRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "活动编号")
    private String activityId;
}
