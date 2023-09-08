package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@ApiModel(value = "文件下载返回实体")
public class DownloadResponse {
    @ApiModelProperty(value = "下载路径")
    private String filePath;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "关系编号")
    private String relationId;
}
