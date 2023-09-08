package com.Hebut.JiangXin.project.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * @author admin
 */
@Data
@ApiModel(value = "查看学生返回实体")
public class CheckStudentResponse {

    @ApiModelProperty(value = "批次名称")
    private String batchName;

    @ApiModelProperty(value = "学工号")
    private String userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "用户类型")
    private String Type;

    @ApiModelProperty(value = "用户所在学院")
    private String institute;

    @ApiModelProperty(value = "用户所在专业")
    private String major;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "批次结束时间")
    private LocalDateTime endTime;

    public static Comparator<CheckStudentResponse> studentId = new Comparator<CheckStudentResponse>() {

        @Override
        public int compare(CheckStudentResponse s1, CheckStudentResponse s2) {
            int stuNo1 = Integer.parseInt(s1.getUserId());
            int stuNo2 = Integer.parseInt(s2.getUserId());
            return stuNo1 - stuNo2;
        }
    };

    public static Comparator<CheckStudentResponse> BatchName = new Comparator<CheckStudentResponse>() {

        @Override
        public int compare(CheckStudentResponse s1, CheckStudentResponse s2) {
            if (s1.getEndTime().isBefore(s2.getEndTime())) {
                return 1;
            } else {
                return -1;
            }
        }
    };

}
