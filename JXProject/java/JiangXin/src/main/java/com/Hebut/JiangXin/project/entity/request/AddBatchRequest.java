package com.Hebut.JiangXin.project.entity.request;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lidong
 */
@Data
@ApiModel(value = "增加批次请求实体")
public class AddBatchRequest {

    @ApiModelProperty(value = "验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "批次名称")
    private String batchName;

    @ApiModelProperty(value = "批次名称")
    private String batchTheme;

    @ApiModelProperty(value = "征集阶段开始时间")
    private LocalDateTime selectBeginning;

    @ApiModelProperty(value = "报名阶段开始时间")
    private LocalDateTime enrollBeginning;

    @ApiModelProperty(value = "中期阶段开始时间")
    private LocalDateTime mediumBeginning;

    @ApiModelProperty(value = "答辩阶段开始时间")
    private LocalDateTime defendBeginning;

    @ApiModelProperty(value = "征集阶段截止时间")
    private LocalDateTime selectDeadline;

    @ApiModelProperty(value = "报名阶段截止时间")
    private LocalDateTime enrollDeadline;

    @ApiModelProperty(value = "中期阶段开始时间")
    private LocalDateTime mediumDeadline;

    @ApiModelProperty(value = "答辩阶段开始时间")
    private LocalDateTime defendDeadline;

    private void SelectLogic(LocalDateTime s, LocalDateTime f) {
        if (s.isAfter(f)) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "时间逻辑错误");
        }
    }

    private void selectLogic(LocalDateTime s, LocalDateTime f) {
        if (s.isAfter(f)) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "时间逻辑错误");
        }
    }

    private void enrollLogic(LocalDateTime s, LocalDateTime f) {
        if (s.isAfter(f)) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "时间逻辑错误");
        }
    }

    private void mediumLogic(LocalDateTime s, LocalDateTime f) {
        if (s.isAfter(f)) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "时间逻辑错误");
        }
    }

    private void defendLogic(LocalDateTime s, LocalDateTime f) {
        if (s.isAfter(f)) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "时间逻辑错误");
        }
    }

    public void timeLogic() {
        selectLogic(this.getSelectBeginning(), this.getSelectDeadline());
        enrollLogic(this.getSelectBeginning(), this.getSelectDeadline());
        mediumLogic(this.getSelectBeginning(), this.getSelectDeadline());
        defendLogic(this.getSelectBeginning(), this.getSelectDeadline());
        if (this.getSelectDeadline().isAfter(this.getEnrollBeginning())) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "时间逻辑错误");
        }
        if (this.getEnrollDeadline().isAfter(this.getMediumBeginning())) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "时间逻辑错误");
        }
        if (this.getMediumDeadline().isAfter(this.getDefendDeadline())) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "时间逻辑错误");
        }
    }


}
