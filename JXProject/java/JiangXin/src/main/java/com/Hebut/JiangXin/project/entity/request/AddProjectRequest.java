package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "增加项目请求实体")
public class AddProjectRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "指导教师")
    private List<String> projectTeacher;

    @ApiModelProperty(value = "项目内容")
    private String projectIntroduction;

    @ApiModelProperty(value = "项目来源")
    private String projectOrigin;

    @ApiModelProperty(value = "需求专业")
    private List<String> projectNeedMajor;

    @ApiModelProperty(value = "报名人数")
    private int projectNeedPeople;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "可报销金额")
    private BigDecimal amount;
}
