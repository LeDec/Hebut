package com.Hebut.JiangXin.project.entity.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "显示所有已报名成员返回实体")
public class ShowAllApplicantResponse {

    @ApiModelProperty(value = "学工号")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "用户类型")
    private String Type;

    @ApiModelProperty(value = "用户所在学院")
    private String institute;

    @ApiModelProperty(value = "用户所在专业")
    private String major;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "是否审核通过")
    private String isAudit;

    @ApiModelProperty(value = "报名表编号")
    private String applicantId;

}
