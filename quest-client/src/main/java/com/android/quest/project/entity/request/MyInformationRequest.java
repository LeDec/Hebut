package com.android.quest.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询个人信息请求实体类")
public class MyInformationRequest {
    @ApiModelProperty(value = "用户编号")
    private String user_id;
}
