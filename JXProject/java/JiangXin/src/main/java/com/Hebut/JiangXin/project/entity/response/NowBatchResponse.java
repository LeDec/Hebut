package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "当前批次返回实体")
public class NowBatchResponse {

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "批次名称")
    private String batchName;

    @ApiModelProperty(value = "批次主题")
    private String batchTheme;

    @ApiModelProperty(value = "批次阶段")
    private String batchStage;

    @ApiModelProperty(value = "批次阶段编号")
    private String batchStageId;
}
