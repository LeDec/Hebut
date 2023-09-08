package com.Hebut.JiangXin.project.entity.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看学生用户信息返回实体分页")
public class CheckClassmatesResponsePage {

    @ApiModelProperty(value = "查看学生用户信息返回实体分页")
    private Page<CheckClassmatesResponse> checkClassmatesResponsePage;

    @ApiModelProperty(value = "页数")
    private Long pages;
}
