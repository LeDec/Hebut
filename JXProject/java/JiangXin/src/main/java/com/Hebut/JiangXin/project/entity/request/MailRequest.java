package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "发送邮件实体类")
public class MailRequest {

    @ApiModelProperty(value = "用户学号")
    private String userId;

}
