package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查询教师指导的项目返回实体")
public class SearchGuideProjectResponse {
    @ApiModelProperty(value = "批次名称")
    private String batchName;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

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

    @ApiModelProperty(value = "当前阶段")
    private String stage;

    @ApiModelProperty(value = "审核状态")
    private String audit;

    @ApiModelProperty(value = "已报销金额")
    private BigDecimal amount;
}