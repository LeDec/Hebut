package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckPastProjectScoreResponse;
import com.Hebut.JiangXin.project.entity.response.CheckProjectAverageResponse;
import com.Hebut.JiangXin.project.entity.response.CheckProjectScoreResponse;
import com.Hebut.JiangXin.project.service.IProjectScoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 项目打分表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-03-07
 */
@RestController
@RequestMapping("/sys/project-score")
@CrossOrigin
@Api(tags = "项目审核得分相关")
public class ProjectScoreController {

    @Resource
    IProjectScoreService iProjectScoreService;


    @PostMapping("/giveProjectScore")
    @ApiOperation(value = "专家评分项目", tags = "项目审核得分相关")
    public ApiResponse distributeProject(
            @RequestBody GiveProjectScoreRequest giveProjectScoreRequest
    ) {
        iProjectScoreService.giveProjectScore(giveProjectScoreRequest);
        return ApiResponse.success();
    }

    @PostMapping("/submitProjectScore")
    @ApiOperation(value = "专家提交评分项目", tags = "项目审核得分相关")
    public ApiResponse submitProjectScore(
            @RequestBody SubmitProjectScoreRequest submitProjectScoreRequest
    ) {
        iProjectScoreService.submitProjectScore(submitProjectScoreRequest);
        return ApiResponse.success();
    }

    @PostMapping("/checkProjectScore")
    @ApiOperation(value = "查看专家评分项目列表", tags = "项目审核得分相关")
    public ApiResponse<PageUtil<CheckProjectScoreResponse>> checkProjectScore(
            @RequestBody CheckProjectScoreRequest checkProjectScoreRequest
    ) {
        PageUtil<CheckProjectScoreResponse> checkProjectScoreResponsePage = iProjectScoreService.checkProjectScore(checkProjectScoreRequest);
        return ApiResponse.success(checkProjectScoreResponsePage);
    }

    @PostMapping("/checkProjectAverage")
    @ApiOperation(value = "查看项目评分均分列表", tags = "项目审核得分相关")
    public ApiResponse<PageUtil<CheckProjectAverageResponse>> checkProjectAverage(
            @RequestBody CheckProjectAverageRequest checkProjectAverageRequest
    ) {
        PageUtil<CheckProjectAverageResponse> checkProjectScoreResponsePage = iProjectScoreService.checkProjectAverage(checkProjectAverageRequest);
        return ApiResponse.success(checkProjectScoreResponsePage);
    }

    @PostMapping("/checkPastProjectScore")
    @ApiOperation(value = "查看往期评分项目列表", tags = "项目审核得分相关")
    public ApiResponse<PageUtil<CheckPastProjectScoreResponse>> checkPastProjectScore(
            @RequestBody CheckPastProjectScoreRequest checkPastProjectScoreRequest
    ) {
        PageUtil<CheckPastProjectScoreResponse> checkPastProjectScoreResponsePage = iProjectScoreService.checkPastProjectScore(checkPastProjectScoreRequest);
        return ApiResponse.success(checkPastProjectScoreResponsePage);
    }

}
