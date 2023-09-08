package com.Hebut.JiangXin.project.entity.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "显示非匠心项目返回实体")
public class ShowAllOtherProjectResponse {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "申请人学工号")
    private String userId;

    @ApiModelProperty(value = "项目内容")
    private String projectIntroduction;

    @ApiModelProperty(value = "指导教师")
    private List<String> projectTeacher;

    @ApiModelProperty(value = "项目来源")
    private String projectOrigin;

    @ApiModelProperty(value = "需求专业")
    private List<String> projectNeedMajor;

    @ApiModelProperty(value = "报名人数")
    private int projectNeedPeople;

    @ApiModelProperty(value = "关系编号")
    private String relationId;

    @ApiModelProperty(value = "当前阶段")
    private String stage;
}
