package com.Hebut.JiangXin.project.entity.request;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@Api(tags = "分页")
public class PageRequest {
    @ApiModelProperty(value = "当前页数")
    private int currentPage;

    @ApiModelProperty(value = "每页最大数据数")
    private int pageSize;
}
