package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.project.entity.request.AddActivityRequest;
import com.Hebut.JiangXin.project.entity.request.CheckActivityRequest;
import com.Hebut.JiangXin.project.entity.request.DeleteActivityRequest;
import com.Hebut.JiangXin.project.entity.response.CheckActivityResponse;
import com.Hebut.JiangXin.project.service.IActivityService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-02-06
 */
@RestController
@RequestMapping("/project/activity")
@CrossOrigin
@Api(tags = "活动相关")
public class ActivityController {

    @Resource
    IActivityService iActivityService;

    @PostMapping(value = "/addActivity")
    @ApiOperation(value = "增加活动", tags = "班主任活动功能")
    public ApiResponse addActivity(
            @RequestBody AddActivityRequest addActivityRequest
    ) {
        iActivityService.addActivity(addActivityRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkActivity")
    @ApiOperation(value = "查看活动列表", tags = "班主任活动功能")
    public ApiResponse<Page<CheckActivityResponse>> checkActivity(
            @RequestBody CheckActivityRequest checkActivityRequest
    ) {
        Page<CheckActivityResponse> checkActivityResponsePage = iActivityService.checkActivity(checkActivityRequest);
        return ApiResponse.success(checkActivityResponsePage);
    }

    @PostMapping(value = "/deleteActivity")
    @ApiOperation(value = "删除活动", tags = "班主任活动功能")
    public ApiResponse addActivity(
            @RequestBody DeleteActivityRequest deleteActivityRequest
    ) {
        iActivityService.deleteActivity(deleteActivityRequest);
        return ApiResponse.success();
    }
}
