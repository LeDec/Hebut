package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查看未给成绩学生名单请求实体")
public class CheckStudentScoreRequest {

    @ApiModelProperty(value = "验证请求")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "分页")
    private PageRequest pageRequest;
}
