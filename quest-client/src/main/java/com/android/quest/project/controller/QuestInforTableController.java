package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.CheckQuestInformationRequest;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.response.CheckQuestInforResponse;
import com.android.quest.project.entity.response.CheckQuestInformationResponse;
import com.android.quest.project.service.IQuestInforTableService;
import com.android.quest.project.service.IQuestTableService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@RestController
@RequestMapping("/project/quest-infor-table")
@CrossOrigin
public class QuestInforTableController {
    @Resource
    IQuestInforTableService iQuestInforTableService;



    @PostMapping(value = "/checkQuestInfor")
    @ApiOperation(value = "查看打卡明细", tags = "任务明细功能")
    public ApiResponse<List<CheckQuestInforResponse>> checkQuestInfor(
            @RequestBody CheckRequest checkRequest
    ) {
        List<CheckQuestInforResponse> checkQuestInforResponseList = iQuestInforTableService.checkQuestInfor(checkRequest);
        return ApiResponse.success(checkQuestInforResponseList);
    }

}
