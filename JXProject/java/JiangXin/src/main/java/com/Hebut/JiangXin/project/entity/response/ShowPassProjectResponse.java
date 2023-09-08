package com.Hebut.JiangXin.project.entity.response;

/**
 * @author lidong
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "显示初试已通过项目返回实体")
public class ShowPassProjectResponse {

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目内容")
    private String projectIntroduction;

    @ApiModelProperty(value = "指导教师")
    private List<String> projectTeacher;

    @ApiModelProperty(value = "项目来源")
    private String projectOrigin;

    @ApiModelProperty(value = "关系编号")
    private String stage;

}
