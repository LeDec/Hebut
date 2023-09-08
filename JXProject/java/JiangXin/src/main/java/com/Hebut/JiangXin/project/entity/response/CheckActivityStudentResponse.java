package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Comparator;

@Data
@ApiModel(value = "查看活动学生列表返回实体")
public class CheckActivityStudentResponse {

    @ApiModelProperty(value = "学工号")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "用户所在学院")
    private String institute;

    @ApiModelProperty(value = "用户所在专业")
    private String major;

    @ApiModelProperty(value = "状态名称")
    private String signStage;

    @ApiModelProperty(value = "状态编号")
    private String signId;

    public static Comparator<CheckActivityStudentResponse> stage = new Comparator<CheckActivityStudentResponse>() {

        @Override
        public int compare(CheckActivityStudentResponse s1, CheckActivityStudentResponse s2) {
            int stageId1 = Integer.parseInt(s1.getSignId());
            int stageId2 = Integer.parseInt(s2.getSignId());
            return stageId1 - stageId2;
        }
    };


}
