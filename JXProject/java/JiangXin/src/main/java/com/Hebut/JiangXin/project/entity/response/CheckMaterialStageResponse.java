package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看提交资料状态返回实体")
public class CheckMaterialStageResponse {

    @ApiModelProperty(value = "提交资料阶段名称")
    private String stage;

    @ApiModelProperty(value = "提交资料状态")
    private String levelId;

    @ApiModelProperty(value = "提交资料阶段")
    private String stageId;

    @ApiModelProperty(value = "项目资料编号")
    private String PCrelationId;

    @ApiModelProperty(value = "提交人学号")
    private String studentId;

    @ApiModelProperty(value = "提交人姓名")
    private String studentName;
}
