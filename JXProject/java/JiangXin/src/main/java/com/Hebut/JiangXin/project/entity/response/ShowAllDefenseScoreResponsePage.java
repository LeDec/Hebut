package com.Hebut.JiangXin.project.entity.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "显示各项目答辩成绩返回实体分页")
public class ShowAllDefenseScoreResponsePage {

    @ApiModelProperty(value = "显示各项目答辩成绩返回分页")
    private Page<ShowAllDefenseScoreResponse> showAllDefenseScoreResponsePage;

    @ApiModelProperty(value = "页数")
    private Long pages;
}
