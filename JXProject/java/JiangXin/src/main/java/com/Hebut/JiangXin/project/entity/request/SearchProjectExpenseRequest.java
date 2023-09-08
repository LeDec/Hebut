package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "搜索项目报销请求实体")
@Data
public class SearchProjectExpenseRequest {

    @ApiModelProperty(value = "查询请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "年")
    private String year;

    @ApiModelProperty(value = "月")
    private String month;

    @ApiModelProperty(value = "日")
    private String day;

    @ApiModelProperty(value = "报销内容")
    private String expense;
}
