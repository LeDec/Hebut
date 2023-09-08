package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckBatchResponse;
import com.Hebut.JiangXin.project.entity.response.CheckTimeResponse;
import com.Hebut.JiangXin.project.entity.response.NowBatchResponse;
import com.Hebut.JiangXin.project.service.IBatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 批次表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-01-27
 */
@RestController
@RequestMapping("/project/batch")
@CrossOrigin
@Api(tags = "批次相关")
public class BatchController {

    @Resource
    IBatchService iBatchService;

    @PostMapping(value = "/addBatch")
    @ApiOperation(value = "增加批次")
    public ApiResponse addBatch(
            @RequestBody AddBatchRequest addBatchRequest
    ) {
        iBatchService.addBatch(addBatchRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkBatch")
    @ApiOperation(value = "查看往期批次")
    public ApiResponse<PageUtil<CheckBatchResponse>> checkBatch(
            @RequestBody CheckBatchRequest checkBatchRequest
    ) {
        PageUtil<CheckBatchResponse> checkBatchResponsePage = iBatchService.checkBatch(checkBatchRequest);
        return ApiResponse.success(checkBatchResponsePage);
    }

    @PostMapping(value = "/setBatchTime")
    @ApiOperation(value = "设置批次时间")
    public ApiResponse setTime(
            @RequestBody SetBatchTimeRequest setBatchTimeRequest
    ) {
        iBatchService.setTime(setBatchTimeRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkTime")
    @ApiOperation(value = "查看时间")
    public ApiResponse<CheckTimeResponse> checkTime(
            @RequestBody CheckTimeRequest checkTimeRequest
    ) {
        CheckTimeResponse checkTimeResponse = iBatchService.checkTime(checkTimeRequest);
        return ApiResponse.success(checkTimeResponse);
    }

    @PostMapping(value = "/pushNotification")
    @ApiOperation(value = "推送项目")
    public ApiResponse pushNotification(
            @RequestBody PushNotificationRequest pushNotificationRequest
    ) {
        iBatchService.pushNotification(pushNotificationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/nowBatch")
    @ApiOperation(value = "当前批次")
    public ApiResponse<NowBatchResponse> nowBatch(
            @RequestBody NowBatchRequest nowBatchRequest
    ) {
        NowBatchResponse nowBatchResponse = iBatchService.nowBatch(nowBatchRequest);
        return ApiResponse.success(nowBatchResponse);
    }

}
