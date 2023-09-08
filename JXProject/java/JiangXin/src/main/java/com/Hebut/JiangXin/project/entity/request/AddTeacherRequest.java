package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2021/11/4
 *
 * @author Lidong
 */
@ApiModel(value = "增加教师请求实体")
@Data
public class AddTeacherRequest {

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "类型编号")
    private String typeId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "学院编号")
    private String instituteId;

    @ApiModelProperty(value = "电子邮箱")
    private String eMail;
}
