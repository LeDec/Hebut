package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看项目返回实体")
public class ScanProjectResponse {


    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目来源")
    private String projectOrigin;

    @ApiModelProperty(value = "项目人数")
    private int projectPeople;

    @ApiModelProperty(value = "指导老师")
    private List<String> teachers;

    @ApiModelProperty(value = "需求专业")
    private List<String> majors;

    @ApiModelProperty(value = "当前阶段")
    private String stage;

    @ApiModelProperty(value = "审核状态")
    private String audit;

    @ApiModelProperty(value = "下载路径")
    private String filePath;

    @ApiModelProperty(value = "文件名")
    private String fileName;


}
