package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckInformPageResponse;
import com.Hebut.JiangXin.project.entity.response.CheckInformResponse;
import com.Hebut.JiangXin.project.entity.response.ScanInformResponse;
import com.Hebut.JiangXin.project.service.IInformService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 通知表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-01-01
 */
@RestController
@RequestMapping("/project/inform")
@CrossOrigin
@Api(tags = "通知相关")
public class InformController {

    @Resource
    IInformService informService;

    @PostMapping(value = "/addInform")
    @ApiOperation(value = "发送通知")
    public ApiResponse addInform(
            @RequestBody AddInformRequest addInformRequest
    ) {
        informService.addInform(addInformRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/deleteInform")
    @ApiOperation(value = "删除通知")
    public ApiResponse deleteInform(
            @RequestBody DeleteInformRequest deleteInformRequest
    ) {
        informService.deleteInform(deleteInformRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/setSomeInform")
    @ApiOperation(value = "向某类用户发送通知")
    public ApiResponse setSomeInform(
            @RequestBody SetSomeInformRequest setSomeInformRequest
    ) {
        informService.setSomeInform(setSomeInformRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkInform")
    @ApiOperation(value = "查看通知列表")
    public ApiResponse<Page<CheckInformResponse>> checkInform(
            @RequestBody CheckInformRequest checkInformRequest
    ) {
        Page<CheckInformResponse> checkInformResponse = informService.checkInform(checkInformRequest);
        return ApiResponse.success(checkInformResponse);
    }

    @PostMapping(value = "/scanInform")
    @ApiOperation(value = "查看通知内容")
    public ApiResponse<ScanInformResponse> scanInform(
            @RequestBody ScanInformRequest scanInformRequest
    ) {
        ScanInformResponse scanInformResponse = informService.scanInform(scanInformRequest);
        return ApiResponse.success(scanInformResponse);
    }

    @PostMapping(value = "/checkAllInform")
    @ApiOperation(value = "查看所有通知列表", tags = "管理员通知高级高能")
    public ApiResponse<CheckInformPageResponse> checkAllInform(
            @RequestBody CheckRequest checkRequest
    ) {
        CheckInformPageResponse checkInformResponse = informService.checkAllInform(checkRequest);
        return ApiResponse.success(checkInformResponse);
    }

    @PostMapping(value = "/checkInformOfMine")
    @ApiOperation(value = "查看自己发送通知列表", tags = "通知相关功能")
    public ApiResponse<CheckInformPageResponse> checkInformOfMine(
            @RequestBody CheckRequest checkRequest
    ) {
        CheckInformPageResponse checkInformResponse = informService.checkInformOfMine(checkRequest);
        return ApiResponse.success(checkInformResponse);
    }

    @PostMapping(value = "/checkWaitReadCount")
    @ApiOperation(value = "查看未读消息数量", tags = "通知相关功能")
    public ApiResponse<Integer> checkWaitReadCount(
            @RequestBody CommonRequest commonRequest
    ) {
        return ApiResponse.success(informService.checkWaitReadCount(commonRequest));
    }

    @PostMapping(value = "/oneClickRead")
    @ApiOperation(value = "全部已读", tags = "通知相关功能")
    public ApiResponse oneClickRead(
            @RequestBody CommonRequest commonRequest
    ) {
        informService.oneClickRead(commonRequest);
        return ApiResponse.success();
    }


}
