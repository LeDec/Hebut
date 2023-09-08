package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看未给活动成绩学生名单返回实体")
public class CheckRemainScoreResponse {

    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "学生姓名")
    private String userName;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;
}
