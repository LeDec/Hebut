package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看活动签到列表返回实体")
public class CheckActivitySignResponse {

    @ApiModelProperty(value = "学号")
    private String userId;

    @ApiModelProperty(value = "学生姓名")
    private String userName;

    @ApiModelProperty(value = "学生活动情况")
    private String stage;

}
