package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "显示各项目答辩成绩请求实体")
@Data
public class ShowAllDefenseScoreRequest {

    @ApiModelProperty(value = "查询验证")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

}
