package com.Hebut.JiangXin.project.entity.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "显示初试待审核项目请求实体")
@Data
public class ShowAuditProjectRequest {

    @ApiModelProperty(value = "分页")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

}
