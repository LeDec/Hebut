package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "查看通知返回实体")
public class ScanInformResponse {
    @ApiModelProperty(value = "通知标题")
    private String title;

    @ApiModelProperty(value = "通知内容")
    private String information;

    @ApiModelProperty(value = "附件")
    private List<String> attachment;
}
