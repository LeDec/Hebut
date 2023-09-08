package com.Hebut.JiangXin.project.entity.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "显示所有项目返回实体分页")
public class ShowProjectResponsePage {

    @ApiModelProperty(value = "分页")
    private Page<ShowProjectResponse> showProjectResponsePage;

    @ApiModelProperty(value = "页数")
    private Long pages;
}
