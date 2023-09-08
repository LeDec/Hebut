package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查看学生成绩请求实体")
@Data
public class SearchAllScoreRequest {

    @ApiModelProperty(value = "查询请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

}
