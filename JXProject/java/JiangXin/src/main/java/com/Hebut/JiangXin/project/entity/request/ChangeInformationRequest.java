package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "修改个人信息请求实体")
public class ChangeInformationRequest {
    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "新专业")
    private String newMajor;

    @ApiModelProperty(value = "新学院")
    private String newInstitute;

    @ApiModelProperty(value = "新邮箱")
    private String newEMail;


}
