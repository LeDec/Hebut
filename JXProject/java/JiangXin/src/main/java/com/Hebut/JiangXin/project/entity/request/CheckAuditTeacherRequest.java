package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "搜索待审核教师信息请求实体")
public class CheckAuditTeacherRequest {

    @ApiModelProperty(value = "查询请求")
    private CheckRequest checkRequest;
}
