package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckAdviceResponse;
import com.Hebut.JiangXin.project.entity.response.CheckOwnEvaluationResponse;
import com.Hebut.JiangXin.project.entity.response.CheckRankResponse;
import com.Hebut.JiangXin.project.entity.response.EvaluationStageResponse;
import com.Hebut.JiangXin.project.service.IEvaluationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 教评表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-01-04
 */
@RestController
@RequestMapping("/project/evaluation")
@Api(tags = "教评相关")
@CrossOrigin
public class EvaluationController {

    @Resource
    IEvaluationService iEvaluationService;


    @PostMapping(value = "/publishEvaluation")
    @ApiOperation(value = "发布教评")
    public ApiResponse publishEvaluation(
            @RequestBody PublishEvaluationRequest publishEvaluationRequest) {
        iEvaluationService.publishEvaluation(publishEvaluationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/changeEvaluationTime")
    @ApiOperation(value = "修改教评时间")
    public ApiResponse changeEvaluationTime(
            @RequestBody ChangeEvaluationTimeRequest changeEvaluationTimeRequest
    ) {
        iEvaluationService.changeEvaluationTime(changeEvaluationTimeRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/evaluationStage")
    @ApiOperation(value = "查询教评状态")
    public ApiResponse<PageUtil<EvaluationStageResponse>> evaluationStage(
            @RequestBody EvaluationStageRequest evaluationStageRequest) {
        PageUtil<EvaluationStageResponse> evaluationStageResponsePage = iEvaluationService.evaluationStage(evaluationStageRequest);
        return ApiResponse.success(evaluationStageResponsePage);
    }

    @PostMapping(value = "/fillEvaluation")
    @ApiOperation(value = "填写教评")
    public ApiResponse addEvaluation(
            @RequestBody AddEvaluationRequest addEvaluationRequest) {
        iEvaluationService.fillEvaluation(addEvaluationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkRank")
    @ApiOperation(value = "查看教评排名", tags = "教评管理员功能")
    public ApiResponse<PageUtil<CheckRankResponse>> checkRank(
            @RequestBody CheckRankRequest checkRankRequest) {
        PageUtil<CheckRankResponse> checkRankResponsePage = iEvaluationService.checkRank(checkRankRequest);
        return ApiResponse.success(checkRankResponsePage);
    }

    @PostMapping(value = "/checkAdvice")
    @ApiOperation(value = "查看教师教评意见", tags = "教评管理员功能")
    public ApiResponse<PageUtil<CheckAdviceResponse>> checkAdvice(
            @RequestBody CheckAdviceRequest checkAdviceRequest) {
        PageUtil<CheckAdviceResponse> checkAdviceResponsePage = iEvaluationService.checkAdvice(checkAdviceRequest);
        return ApiResponse.success(checkAdviceResponsePage);
    }

    @PostMapping(value = "/checkOwnEvaluation")
    @ApiOperation(value = "查询自己教评", tags = "教评指导老师功能")
    public ApiResponse<PageUtil<CheckOwnEvaluationResponse>> checkOwnEvaluation(
            @RequestBody CheckOwnEvaluationRequest checkOwnEvaluationRequest) {
        PageUtil<CheckOwnEvaluationResponse> checkOwnEvaluationResponsePage = iEvaluationService.checkOwnEvaluation(checkOwnEvaluationRequest);
        return ApiResponse.success(checkOwnEvaluationResponsePage);
    }
}
