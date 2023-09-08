package com.Hebut.JiangXin.project.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "提交学生成绩请求实体")
public class SubmitScoreRequest {

    @ApiModelProperty(value = "登录验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "项目编号")
    private List<String> projectId;

    @ApiModelProperty(value = "学生学号")
    private List<String> userId;

    @ApiModelProperty(value = "该项成绩")
    private List<Float> score;
}
