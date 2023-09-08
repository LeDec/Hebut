package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查询项目请求实体")
@Data
public class SearchProjectRequest {

    @ApiModelProperty(value = "分页")
    private PageRequest pageRequest;

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "项目编号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "指导教师")
    private String projectTeachers;

    @ApiModelProperty(value = "项目来源")
    private String projectOrigin;

    @ApiModelProperty(value = "需求专业")
    private String projectMajors;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "批次Id")
    private String batchId;

}
