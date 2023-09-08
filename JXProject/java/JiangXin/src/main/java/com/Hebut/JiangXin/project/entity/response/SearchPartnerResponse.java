package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查询教师指导的项目返回实体")
public class SearchPartnerResponse {

    @ApiModelProperty(value = "报名编号")
    private String applicationId;

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

}
