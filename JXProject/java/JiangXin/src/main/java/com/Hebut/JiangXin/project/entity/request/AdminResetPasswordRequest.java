package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 2021/11/4
 *
 * @author Lidong
 */
@ApiModel(value = "管理员重置密码请求实体")
@Data
public class AdminResetPasswordRequest {

    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;
}
