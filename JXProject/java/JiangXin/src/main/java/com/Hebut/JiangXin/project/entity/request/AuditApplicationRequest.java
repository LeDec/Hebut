package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 2021/11/4
 *
 * @author Lidong
 */
@ApiModel(value = "审核报名请求实体")
@Data
public class AuditApplicationRequest {

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "是否通过（1、通过；0、不通过）")
    private String isPass;

    @ApiModelProperty(value = "报名编号")
    private List<String> applicantId;
}
