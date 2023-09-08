package com.Hebut.JiangXin.project.entity.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "教师信息分页返回实体")
public class TeacherInformationPageResponse {

    @ApiModelProperty(value = "教师信息返回实体")
    private Page<TeacherInformationResponse> teacherInformationResponse;

    @ApiModelProperty(value = "页数")
    private Long pages;
}
