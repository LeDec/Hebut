package com.android.quest.project.entity.request;

import com.android.quest.project.entity.NoteTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "增加通知请求实体类")
public class AddNoteRequest {

    @ApiModelProperty(value = "发布人编号")
    private int adminId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "文章")
    private String article;

}
