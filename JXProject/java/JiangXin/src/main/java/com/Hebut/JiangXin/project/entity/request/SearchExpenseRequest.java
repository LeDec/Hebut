package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查看报销请求实体")
@Data
public class SearchExpenseRequest {

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "分页")
    private PageRequest pageRequest;

    @ApiModelProperty(value = "年")
    private String year;

    @ApiModelProperty(value = "月")
    private String month;

    @ApiModelProperty(value = "日")
    private String day;

    @ApiModelProperty(value = "报销内容")
    private String expense;

}
