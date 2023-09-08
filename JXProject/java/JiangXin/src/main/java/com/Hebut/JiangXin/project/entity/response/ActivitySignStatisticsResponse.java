package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "统计活动签到返回实体")
public class ActivitySignStatisticsResponse {

    @ApiModelProperty(value = "活动编号")
    private String activityId;

    @ApiModelProperty(value = "活动标题")
    private String activityTitle;

    @ApiModelProperty(value = "参加人数")
    private int participate;

    @ApiModelProperty(value = "已签到人数")
    private int signOn;

}
