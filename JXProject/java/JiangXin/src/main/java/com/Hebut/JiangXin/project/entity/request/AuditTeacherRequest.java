package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "审核教师注册请求实体")
public class AuditTeacherRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "用户学工号")
    private String useId;

    @ApiModelProperty(value = "通过与否")
    private String isPass;
}
