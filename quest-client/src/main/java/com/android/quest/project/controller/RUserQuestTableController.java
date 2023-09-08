package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.CheckUserQuestRequest;
import com.android.quest.project.entity.request.CollectTotalRequest;
import com.android.quest.project.entity.request.UpdateQuestRequest;
import com.android.quest.project.entity.response.CheckUserQuestResponse;
import com.android.quest.project.service.IRUserQuestTableService;
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
@RequestMapping("/project/r-user-quest-table")
@CrossOrigin
public class RUserQuestTableController {

    @Resource
    IRUserQuestTableService irUserQuestTableService;


    @PostMapping(value = "/collectTotal")
    @ApiOperation(value = "收获完成全部任务奖励", tags = "任务功能")
    public ApiResponse collectTotal(
            @RequestBody CollectTotalRequest collectTotalRequest
    ) {
        irUserQuestTableService.collectTotal(collectTotalRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/updateQuest")
    @ApiOperation(value = "更新任务", tags = "任务功能")
    public ApiResponse updateQuest(
            @RequestBody UpdateQuestRequest updateQuestRequest
    ) {
        irUserQuestTableService.updateQuest(updateQuestRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkUserQuest")
    @ApiOperation(value = "查看用户任务", tags = "web端接口")
    public ApiResponse<CheckUserQuestResponse> checkUserQuest(
            @RequestBody CheckUserQuestRequest checkUserQuestRequest
    ) {
        CheckUserQuestResponse checkUserQuestResponse =irUserQuestTableService.checkUserQuest(checkUserQuestRequest);
        return ApiResponse.success(checkUserQuestResponse);
    }


}
