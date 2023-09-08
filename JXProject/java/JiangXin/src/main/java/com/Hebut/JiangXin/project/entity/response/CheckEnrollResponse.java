package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Comparator;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "查询报名返回实体")
public class CheckEnrollResponse {

    @ApiModelProperty(value = "报名编号")
    private String enrollId;

    @ApiModelProperty(value = "项目编号")
    private String projectId;

    @ApiModelProperty(value = "项目代号")
    private String projectSymbol;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "用户编号")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "学院")
    private String institute;

    @ApiModelProperty(value = "报名凭证")
    private String certificate;

    @ApiModelProperty(value = "报名状态")
    private String enrollStage;

    @ApiModelProperty(value = "是否通过")
    private String isPass;

    @ApiModelProperty(value = "状态编号")
    private String relationId;

    public static Comparator<CheckEnrollResponse> RelationId = new Comparator<CheckEnrollResponse>() {

        @Override
        public int compare(CheckEnrollResponse s1, CheckEnrollResponse s2) {
            int stuNo1 = Integer.parseInt(s1.getRelationId());
            int stuNo2 = Integer.parseInt(s2.getRelationId());
            return stuNo1 - stuNo2;
        }
    };

}
