package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.AddNoteRequest;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.response.CheckNoteListResponse;
import com.android.quest.project.service.INoteTableService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@RestController
@RequestMapping("/project/note-table")
@CrossOrigin
public class NoteTableController {

    @Resource
    INoteTableService iNoteTableService;

    @PostMapping(value = "/checkNoteList")
    @ApiOperation(value = "查看通知列表", tags = "web端接口")
    public ApiResponse<CheckNoteListResponse> checkNoteList(
            @RequestBody CheckRequest checkRequest
    ) {
        CheckNoteListResponse checkNoteListResponse = iNoteTableService.checkNoteList(checkRequest);
        return ApiResponse.success(checkNoteListResponse);
    }

    @PostMapping(value = "/addNote")
    @ApiOperation(value = "增加通知", tags = "web端接口")
    public ApiResponse addNote(
            @RequestBody AddNoteRequest addNoteRequest
    ) {
        iNoteTableService.addNote(addNoteRequest);
        return ApiResponse.success();
    }

}
