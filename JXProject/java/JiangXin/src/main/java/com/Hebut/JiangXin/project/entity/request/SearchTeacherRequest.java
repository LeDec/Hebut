package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@ApiModel(value = "查询教师用户请求实体")
@Data
public class SearchTeacherRequest {

    @ApiModelProperty(value = "查询请求")
    private CheckRequest checkRequest;

    @ApiModelProperty(value = "姓名")
    private String teacherName;

    @ApiModelProperty(value = "工号")
    private String teacherId;

    @ApiModelProperty(value = "学院")
    private String institute;

    @ApiModelProperty(value = "角色")
    private String userType;

}
