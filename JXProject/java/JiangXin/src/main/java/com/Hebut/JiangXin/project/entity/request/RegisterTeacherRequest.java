package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2021/11/4
 *
 * @author Lidong
 */
@ApiModel(value = "教师注册请求实体")
@Data
public class RegisterTeacherRequest {

    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "重复密码")
    private String passwordRepeat;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "学院编号")
    private String instituteId;

    @ApiModelProperty(value = "电子邮箱")
    private String eMail;

    @ApiModelProperty(value = "注册身份")
    private String userType;
}
