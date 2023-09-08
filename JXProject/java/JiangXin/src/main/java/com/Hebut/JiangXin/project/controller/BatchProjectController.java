package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.project.entity.request.CheckProjectTimeRequest;
import com.Hebut.JiangXin.project.entity.request.IsNowBatchRequest;
import com.Hebut.JiangXin.project.entity.request.SetProjectTimeRequest;
import com.Hebut.JiangXin.project.entity.response.CheckProjectTimeResponse;
import com.Hebut.JiangXin.project.service.IBatchProjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 批次项目表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-01-27
 */
@RestController
@RequestMapping("/project/batch-project")
@CrossOrigin
public class BatchProjectController {

    @Resource
    IBatchProjectService iBatchProjectService;

    @PostMapping(value = "/isNowBatch")
    @ApiOperation(value = "判断该项目是否为本期项目", tags = "报销相关老师功能")
    public ApiResponse isNowBatch(
            @RequestBody IsNowBatchRequest isNowBatchRequest
    ) {
        iBatchProjectService.isNowBatch(isNowBatchRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/setProjectTime")
    @ApiOperation(value = "设置项目时间")
    public ApiResponse setProjectTime(
            @RequestBody SetProjectTimeRequest setProjectTimeRequest
    ) {
        iBatchProjectService.setProjectTime(setProjectTimeRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkProjectTime")
    @ApiOperation(value = "查看项目时间")
    public ApiResponse<CheckProjectTimeResponse> checkProjectTime(
            @RequestBody CheckProjectTimeRequest checkProjectTimeRequest
    ) {
        CheckProjectTimeResponse checkProjectTimeResponse = iBatchProjectService.checkProjectTime(checkProjectTimeRequest);
        return ApiResponse.success(checkProjectTimeResponse);
    }

}
