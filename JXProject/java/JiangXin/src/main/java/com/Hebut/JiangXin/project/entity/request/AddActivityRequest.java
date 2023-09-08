package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "增加活动请求实体")
public class AddActivityRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "活动标题")
    private String activityTitle;

    @ApiModelProperty(value = "活动介绍")
    private String activityIntroduction;

    @ApiModelProperty(value = "起始时间")
    private LocalDateTime beginning;

    @ApiModelProperty(value = "截止时间")
    private LocalDateTime deadline;

}
