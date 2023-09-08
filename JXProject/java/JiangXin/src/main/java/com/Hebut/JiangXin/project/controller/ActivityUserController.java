package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.ActivityActivityStatisticsRequest;
import com.Hebut.JiangXin.project.entity.request.CheckActivityStudentRequest;
import com.Hebut.JiangXin.project.entity.request.CheckSignOnRequest;
import com.Hebut.JiangXin.project.entity.request.SignOnActivityRequest;
import com.Hebut.JiangXin.project.entity.response.ActivitySignStatisticsResponse;
import com.Hebut.JiangXin.project.entity.response.CheckActivitySignResponse;
import com.Hebut.JiangXin.project.entity.response.CheckActivityStudentResponse;
import com.Hebut.JiangXin.project.service.IActivityUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 活动参与表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-02-06
 */
@RestController
@RequestMapping("/project/activity-user")
@CrossOrigin
public class ActivityUserController {

    @Resource
    IActivityUserService iActivityUserService;

    @PostMapping(value = "/signOnActivity")
    @ApiOperation(value = "活动学生签到", tags = "班主任活动功能")
    public ApiResponse signOnActivity(
            @RequestBody SignOnActivityRequest signOnActivityRequest
    ) {
        iActivityUserService.signOnActivity(signOnActivityRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkActivitySign")
    @ApiOperation(value = "查看活动签到列表", tags = "班主任活动功能")
    public ApiResponse<Page<CheckActivitySignResponse>> checkActivitySign(
            @RequestBody CheckSignOnRequest checkSignOnRequest
    ) {
        Page<CheckActivitySignResponse> checkActivitySignResponsePage = iActivityUserService.checkActivitySign(checkSignOnRequest);
        return ApiResponse.success(checkActivitySignResponsePage);
    }


    @PostMapping(value = "/activityActivityStatistics")
    @ApiOperation(value = "统计活动签到", tags = "班主任活动功能")
    public ApiResponse<Page<ActivitySignStatisticsResponse>> activityActivityStatistics(
            @RequestBody ActivityActivityStatisticsRequest activityActivityStatisticsRequest
    ) {
        Page<ActivitySignStatisticsResponse> activitySignStatisticsResponsePage = iActivityUserService.activityActivityStatistics(activityActivityStatisticsRequest);
        return ApiResponse.success(activitySignStatisticsResponsePage);
    }

    @PostMapping(value = "/checkActivityStudent")
    @ApiOperation(value = "查看活动学生列表", tags = "班主任活动功能")
    public ApiResponse<Page<CheckActivityStudentResponse>> checkActivityStudent(
            @RequestBody CheckActivityStudentRequest checkActivityStudentRequest
    ) {
        Page<CheckActivityStudentResponse> checkActivityStudentResponsePage = iActivityUserService.checkActivityStudent(checkActivityStudentRequest);
        return ApiResponse.success(checkActivityStudentResponsePage);
    }


}
