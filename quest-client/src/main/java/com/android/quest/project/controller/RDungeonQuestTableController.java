package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.*;
import com.android.quest.project.entity.response.CheckDungeonQuestResponse;
import com.android.quest.project.entity.response.CheckUserQuestResponse;
import com.android.quest.project.entity.response.GetQuestResponse;
import com.android.quest.project.service.IRDungeonQuestTableService;
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
@RequestMapping("/project/r-dungeon-quest-table")
public class RDungeonQuestTableController {
    @Resource
    IRDungeonQuestTableService irDungeonQuestTableService;

    @PostMapping(value = "/getDungeonQuest")
    @ApiOperation(value = "获取副本的任务", tags = "副本功能")
    public ApiResponse<GetQuestResponse> getDungeonQuest(
            @RequestBody GetDungeonQuestRequest getDungeonQuestRequest
    ) {
        GetQuestResponse getQuestResponse = irDungeonQuestTableService.getDungeonQuest(getDungeonQuestRequest);
        return ApiResponse.success(getQuestResponse);
    }

    @PostMapping(value = "/checkDungeonQuest")
    @ApiOperation(value = "查看副本可选择任务", tags = "web端接口")
    public ApiResponse<CheckDungeonQuestResponse> checkDungeonQuest(
            @RequestBody CheckRequest checkRequest
    ) {
        CheckDungeonQuestResponse checkDungeonQuestResponse =irDungeonQuestTableService.checkDungeonQuest(checkRequest);
        return ApiResponse.success(checkDungeonQuestResponse);
    }

    @PostMapping(value = "/addDungeonQuest")
    @ApiOperation(value = "增加副本可选任务", tags = "web端接口")
    public ApiResponse addDungeonQuest(
            @RequestBody AddDungeonQuestRequest addDungeonQuestRequest
    ) {
        irDungeonQuestTableService.addDungeonQuest(addDungeonQuestRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/dungeonIntegration")
    @ApiOperation(value = "集成副本", tags = "web端接口")
    public ApiResponse DungeonIntegration(
            @RequestBody DungeonIntegrationRequest dungeonIntegrationRequest
    ) {
        irDungeonQuestTableService.DungeonIntegration(dungeonIntegrationRequest);
        return ApiResponse.success();
    }

}
