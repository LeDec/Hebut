package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "通知信息返回实体")
public class CheckInformResponse {

    @ApiModelProperty(value = "通知编号")
    private String informId;

    @ApiModelProperty(value = "发送人")
    private String sender;

    @ApiModelProperty(value = "接收人")
    private String acceptor;

    @ApiModelProperty(value = "通知标题")
    private String title;

    @ApiModelProperty(value = "通知时间")
    private String informTime;

    @ApiModelProperty(value = "已读状态")
    private Boolean isRead;
}
