package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@Api(tags = "分配专家项目审核任务请求实体")
public class DistributeProjectRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "项目编号列表")
    private List<String> projectIdList;

    @ApiModelProperty(value = "专家工号")
    private String expertId;

}
