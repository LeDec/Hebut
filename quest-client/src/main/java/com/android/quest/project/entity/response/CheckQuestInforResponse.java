package com.android.quest.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel(value = "查看任务明细返回实体类")
public class CheckQuestInforResponse {
    @ApiModelProperty(value = "明细编号")
    private int _id;
    @ApiModelProperty(value = "任务编号")
    private int questId;
    @ApiModelProperty(value = "任务标题")
    private String title;
    @ApiModelProperty(value = "任务类型")
    private String type;
    @ApiModelProperty(value = "打卡时间")
    private String createTime;
}
