package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;


/**
 * @author admin
 */
@Data
@ApiModel(value = "查看用户项目经历返回实体")
public class CheckUserExperienceResponse {

    @ApiModelProperty(value = "项目关系编号")
    private String relationId;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "批次编号")
    private String batchName;

    @ApiModelProperty(value = "匠心项目Id")
    private String projectId;

    @ApiModelProperty(value = "匠心项目编号")
    private String projectSymbol;

    @ApiModelProperty(value = "匠心项目名称")
    private String projectName;

    @ApiModelProperty(value = "批次开始时间")
    private LocalDateTime startTime;

    public static Comparator<CheckUserExperienceResponse> batchTime = new Comparator<CheckUserExperienceResponse>() {

        @Override
        public int compare(CheckUserExperienceResponse s1, CheckUserExperienceResponse s2) {
            if (s1.getStartTime().isAfter(s2.getStartTime())) {
                return 1;
            } else {
                return -1;
            }
        }
    };
}
