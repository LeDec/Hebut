package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.response.GetAchievementResponse;
import com.android.quest.project.service.IRUserAchievementTableService;
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
@RequestMapping("/project/r-user-achievement-table")
@CrossOrigin
public class RUserAchievementTableController {

    @Resource
    IRUserAchievementTableService irUserAchievementTableService;

    @PostMapping(value = "/getAchievementList")
    @ApiOperation(value = "查看个人成就列表", tags = "成就功能")
    public ApiResponse<GetAchievementResponse> getAchievementList(
            @RequestBody CheckRequest checkRequest
    ) {
        GetAchievementResponse getAchievementResponse = irUserAchievementTableService.getAchievementList(checkRequest);
        return ApiResponse.success(getAchievementResponse);
    }

}
