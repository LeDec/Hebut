package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "修改用户信息请求实体")
public class ChangeUserInformationRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "用户学工号")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "目的用户类型")
    private String userType;

    @ApiModelProperty(value = "学院编号")
    private String instituteId;

    @ApiModelProperty(value = "专业编号")
    private String majorId;

    @ApiModelProperty(value = "电子邮箱")
    private String eMail;

}
