package com.android.quest.project.entity.response;

import com.android.quest.project.entity.NoteTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "查看通知列表实体类")
public class CheckNoteListResponse {

    @ApiModelProperty(value = "通知列表")
    private List<NoteTable> noteTableList;
}
