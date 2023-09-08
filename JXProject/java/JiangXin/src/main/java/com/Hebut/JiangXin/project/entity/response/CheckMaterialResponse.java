package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看项目已提交资料返回实体")
public class CheckMaterialResponse {

    @ApiModelProperty(value = "项目材料编号")
    private String projectMaterialId;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "审核阶段")
    private String auditStage;

    @ApiModelProperty(value = "审核阶段编号")
    private String auditStageId;

    @ApiModelProperty(value = "审核阶段编号")
    private String auditLevelId;

    @ApiModelProperty(value = "提交人学号")
    private String studentId;

    @ApiModelProperty(value = "提交人姓名")
    private String studentName;

}
