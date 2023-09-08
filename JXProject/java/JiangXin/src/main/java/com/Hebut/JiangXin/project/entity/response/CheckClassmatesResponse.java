package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Comparator;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看学生用户信息返回实体")
public class CheckClassmatesResponse {

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

    @ApiModelProperty(value = "报名匠心班项目编号")
    private String enrollId;

    @ApiModelProperty(value = "匠心项目Id")
    private String projectId;

    @ApiModelProperty(value = "匠心项目编号")
    private String projectSymbol;

    @ApiModelProperty(value = "匠心项目名称")
    private String projectName;


    public static Comparator<CheckClassmatesResponse> studentId = new Comparator<CheckClassmatesResponse>() {

        @Override
        public int compare(CheckClassmatesResponse s1, CheckClassmatesResponse s2) {
            int stuNo1 = Integer.parseInt(s1.getUserId());
            int stuNo2 = Integer.parseInt(s2.getUserId());
            return stuNo1 - stuNo2;
        }
    };


}
