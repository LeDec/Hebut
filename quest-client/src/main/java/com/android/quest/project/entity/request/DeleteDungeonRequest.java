package com.android.quest.project.entity.request;


import com.android.quest.project.entity.response.CheckDungeonResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "删除副本请求实体类")
public class DeleteDungeonRequest {

    @ApiModelProperty(value = "管理员编号")
    private int adminId;

    @ApiModelProperty(value = "副本编号")
    private int dungeonId;

}
