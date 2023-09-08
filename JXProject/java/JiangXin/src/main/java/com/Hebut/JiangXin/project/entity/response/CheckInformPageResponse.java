package com.Hebut.JiangXin.project.entity.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "通知信息分页返回实体")
public class CheckInformPageResponse {

    @ApiModelProperty(value = "通知信息返回实体")
    private Page<CheckInformResponse> checkInformResponse;

    @ApiModelProperty(value = "发送人")
    private Long pages;
}
