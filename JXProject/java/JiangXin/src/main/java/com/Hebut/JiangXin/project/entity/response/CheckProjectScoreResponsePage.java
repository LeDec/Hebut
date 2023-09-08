package com.Hebut.JiangXin.project.entity.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查看专家未评分项目列表返回实体分页")
public class CheckProjectScoreResponsePage {

    @ApiModelProperty(value = "查看专家未评分项目列表分页")
    private Page<CheckProjectScoreResponse> checkProjectScoreResponsePage;

    @ApiModelProperty(value = "页数")
    private Long pages;
}
