package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.*;
import com.android.quest.project.entity.response.CheckQuestInformationResponse;
import com.android.quest.project.entity.response.CompleteQuestResponse;
import com.android.quest.project.entity.response.GetQuestResponse;
import com.android.quest.project.service.IQuestTableService;
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
@CrossOrigin
@RestController
@RequestMapping("/project/quest-table")
public class QuestTableController {
    @Resource
    IQuestTableService iQuestTableService;

    @PostMapping(value = "/getQuest")
    @ApiOperation(value = "获取任务", tags = "任务功能")
    public ApiResponse<GetQuestResponse> getQuest(
            @RequestBody GetQuestRequest getQuestRequest
    ) {
        GetQuestResponse getQuestResponse = iQuestTableService.getQuest(getQuestRequest);
        return ApiResponse.success(getQuestResponse);
    }


    @PostMapping(value = "/getAppliedDungeonQuest")
    @ApiOperation(value = "获取已参与副本的任务", tags = "任务功能")
    public ApiResponse<GetQuestResponse> getAppliedDungeonQuest(
            @RequestBody GetAppliedDungeonRequest getAppliedDungeonRequest
    ) {
        GetQuestResponse getQuestResponse = iQuestTableService.getAppliedDungeonQuest(getAppliedDungeonRequest);
        return ApiResponse.success(getQuestResponse);
    }

    @PostMapping(value = "/developQuest")
    @ApiOperation(value = "制定任务", tags = "任务功能")
    public ApiResponse developQuest(
            @RequestBody DevelopQuestRequest developQuestRequest
    ) {
        iQuestTableService.developQuest(developQuestRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/completeQuest")
    @ApiOperation(value = "完成任务", tags = "任务功能")
    public ApiResponse<CompleteQuestResponse> completeQuest(
            @RequestBody CompleteQuestRequest completeQuestRequest
    ) {
        CompleteQuestResponse completeQuestResponse = iQuestTableService.completeQuest(completeQuestRequest);
        return ApiResponse.success(completeQuestResponse);
    }

    @PostMapping(value = "/checkQuestInformation")
    @ApiOperation(value = "获得任务信息", tags = "任务功能")
    public ApiResponse<CheckQuestInformationResponse> checkQuestInformation(
            @RequestBody CheckQuestInformationRequest checkQuestInformationRequest
    ) {
        CheckQuestInformationResponse checkQuestInformationResponse = iQuestTableService.checkQuestInformation(checkQuestInformationRequest);
        return ApiResponse.success(checkQuestInformationResponse);
    }
}
