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
@ApiModel(value = "设置批次时间请求实体")
@Data
public class SetBatchTimeRequest {

    @ApiModelProperty(value = "查询验证")
    private CommonRequest commonRequest;

    @ApiModelProperty(value = "批次编号")
    private String batchId;

    @ApiModelProperty(value = "阶段")
    private String batchStage;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginning;

    @ApiModelProperty(value = "截止时间")
    private LocalDateTime deadline;

    public void timeLogic() {
        if (this.getBeginning().isAfter(this.getDeadline())) {
            throw new CustomException(ErrorEnum.TIME_WRONG.getCode(), "时间逻辑错误");
        }
    }
}
