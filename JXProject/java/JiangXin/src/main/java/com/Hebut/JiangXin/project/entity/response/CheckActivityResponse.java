package com.Hebut.JiangXin.project.entity.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看活动列表返回实体")
public class CheckActivityResponse {

    @ApiModelProperty(value = "活动编号")
    private String activityId;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "活动标题")
    private String activityTitle;

    @ApiModelProperty(value = "活动介绍")
    private String activityIntroduction;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginning;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime deadline;

    public static Comparator<CheckActivityResponse> activityTime = new Comparator<CheckActivityResponse>() {

        @Override
        public int compare(CheckActivityResponse s1, CheckActivityResponse s2) {
            if (s1.getBeginning().isAfter(s2.getBeginning())) {
                return -1;
            } else {
                return 1;
            }
        }
    };
}
