package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看指导项目成绩请求实体")
public class CheckGuideScoreRequest {


    @ApiModelProperty(value = "查询验证")
    private CheckRequest checkRequest;

}
