package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "修改教师信息请求实体")
public class ChangeTeacherInformationRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "用户学工号")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String name;


    @ApiModelProperty(value = "学院编号")
    private String instituteId;

    @ApiModelProperty(value = "电子邮箱")
    private String eMail;


}
