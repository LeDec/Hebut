package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "发送通知请求实体")
public class AddInformRequest {
    @ApiModelProperty(value = "发送方学工号")
    private String senderId;

    @ApiModelProperty(value = "接收方编号")
    private List<String> acceptorId;

    @ApiModelProperty(value = "通知标题")
    private String title;

    @ApiModelProperty(value = "通知内容")
    private String information;

    @ApiModelProperty(value = "附件")
    private List<String> attachment;
}
