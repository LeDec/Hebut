package com.Hebut.JiangXin.project.entity.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看学生返回分页实体")
public class CheckStudentResponsePage {

    @ApiModelProperty(value = "查看学生返回实体分页")
    private Page<CheckStudentResponse> checkStudentResponsePage;

    @ApiModelProperty(value = "页数")
    private Long pages;
}
