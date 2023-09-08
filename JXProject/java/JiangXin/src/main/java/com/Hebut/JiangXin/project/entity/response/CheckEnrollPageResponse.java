package com.Hebut.JiangXin.project.entity.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查询报名分页返回实体")
public class CheckEnrollPageResponse {

    @ApiModelProperty(value = "查询报名返回实体")
    private Page<CheckEnrollResponse> checkEnrollResponsePage;

    @ApiModelProperty(value = "页数")
    private long pages;
}
