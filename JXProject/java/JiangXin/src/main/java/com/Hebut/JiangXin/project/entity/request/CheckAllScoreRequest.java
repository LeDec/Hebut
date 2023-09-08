package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看匠心班学生成绩请求实体")
public class CheckAllScoreRequest {


    @ApiModelProperty(value = "查询验证")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;
}
