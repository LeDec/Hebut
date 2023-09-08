package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "修改用户类型请求实体")
public class ChangeUserTypeRequest {

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "用户学号")
    private String userId;

    @ApiModelProperty(value = "目的用户类型")
    private String userType;

}
