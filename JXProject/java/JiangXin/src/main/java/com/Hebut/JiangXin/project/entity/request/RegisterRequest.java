package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 2021/11/4
 *
 * @author Lidong
 */
@ApiModel(value = "注册请求实体")
@Data
public class RegisterRequest {
    @Size(min = 6, max = 7, message = "学工号为6位或者7位")
    @ApiModelProperty(value = "学号")
    private String userId;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "重复密码")
    private String passwordRepeat;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "学院编号")
    private String instituteId;

    @ApiModelProperty(value = "专业编号")
    private String majorId;

    @Email
    @ApiModelProperty(value = "电子邮箱")
    private String eMail;

}
