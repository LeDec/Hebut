package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.service.IScoreService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2021-11-29
 */
@RestController
@RequestMapping("/project/score")
@CrossOrigin
@Api(tags = "成绩相关")
public class ScoreController {

    @Resource
    IScoreService iScoreService;

    @PostMapping("/checkOwnScore")
    @ApiOperation(value = "查询本人成绩", tags = "成绩相关功能")
    public ApiResponse<SearchScoreResponse> checkOwnScore(
            @RequestBody CheckOwnScoreRequest checkOwnScoreRequest
    ) {
        SearchScoreResponse searchScoreResponse = iScoreService.checkOwnScore(checkOwnScoreRequest);
        return ApiResponse.success(searchScoreResponse);
    }

    @PostMapping("/checkStudentScore")
    @ApiOperation(value = "查看项目学生成绩", tags = "管理员成绩相关功能")
    public ApiResponse<Page<CheckStudentScoreResponse>> searchStudentScore(
            @RequestBody SearchAllScoreRequest searchAllScoreRequest
    ) {
        Page<CheckStudentScoreResponse> checkStudentScoreResponsePage = iScoreService.checkStudentScore(searchAllScoreRequest);
        return ApiResponse.success(checkStudentScoreResponsePage);
    }

    @PostMapping("/checkAllScore")
    @ApiOperation(value = "查看匠心班学生成绩", tags = "管理员成绩相关功能")
    public ApiResponse<PageUtil<SearchAllScoreResponse>> searchAllScore(
            @RequestBody CheckAllScoreRequest checkAllScoreRequest
    ) {
        PageUtil<SearchAllScoreResponse> searchAllScoreResponsePage = iScoreService.checkAllScore(checkAllScoreRequest);
        return ApiResponse.success(searchAllScoreResponsePage);
    }

    @PostMapping("/modifyScore")
    @ApiOperation(value = "修改学生成绩", tags = "管理员成绩相关功能")
    public ApiResponse modifyScore(
            @RequestBody ModifyScoreRequest modifyScoreRequest
    ) {
        iScoreService.modifyScore(modifyScoreRequest);
        return ApiResponse.success();
    }

    @PostMapping("/modifyScorePromote")
    @ApiOperation(value = "权限修改学生成绩", tags = "教师成绩相关功能")
    public ApiResponse modifyScorePromote(
            @RequestBody ModifyScoreRequestPromoteRequest modifyScoreRequestPromoteRequest
    ) {
        iScoreService.modifyScorePromote(modifyScoreRequestPromoteRequest);
        return ApiResponse.success();
    }

    @PostMapping("/giveScore")
    @ApiOperation(value = "给定学生成绩", tags = "高级成绩相关功能")
    public ApiResponse giveScore(
            @RequestBody GiveScoreRequest giveScoreRequest
    ) {
        iScoreService.giveScore(giveScoreRequest);
        return ApiResponse.success();
    }

    @PostMapping("/submitScore")
    @ApiOperation(value = "提交学生成绩", tags = "高级成绩相关功能")
    public ApiResponse submitScore(
            @RequestBody SubmitScoreRequest submitScoreRequest
    ) {
        iScoreService.submitScore(submitScoreRequest);
        return ApiResponse.success();
    }

    @PostMapping("/giveDefense")
    @ApiOperation(value = "给定学生答辩成绩", tags = "专家成绩相关功能")
    public ApiResponse giveDefense(
            @RequestBody GiveDefenseRequest giveDefenseRequest
    ) {
        iScoreService.giveDefense(giveDefenseRequest);
        return ApiResponse.success();
    }

    @PostMapping("/submitDefense")
    @ApiOperation(value = "提交学生答辩成绩", tags = "高级成绩相关功能")
    public ApiResponse submitDefense(
            @RequestBody SubmitDefenseRequest submitDefenseRequest
    ) {
        iScoreService.submitDefense(submitDefenseRequest);
        return ApiResponse.success();
    }

    @PostMapping("/setStandard")
    @ApiOperation(value = "设置分数占比", tags = "管理员成绩相关功能")
    public ApiResponse setStandard(
            @RequestBody SetStandardRequest setStandardRequest
    ) {
        iScoreService.setStandard(setStandardRequest);
        return ApiResponse.success();
    }

    @PostMapping("/checkStandard")
    @ApiOperation(value = "查看分数占比", tags = "管理员成绩相关功能")
    public ApiResponse<CheckStandardResponse> checkStandard(
            @RequestBody CommonRequest commonRequest
    ) {
        CheckStandardResponse checkStandardResponse = iScoreService.checkStandard(commonRequest);
        return ApiResponse.success(checkStandardResponse);
    }

    @PostMapping(value = "/showAllDefenseScore")
    @ApiOperation(value = "显示各项目答辩成绩", tags = "专家成绩相关功能")
    public ApiResponse<ShowAllDefenseScoreResponsePage> showAllDefenseScore(
            @RequestBody ShowAllDefenseScoreRequest showAllDefenseScoreRequest
    ) {
        ShowAllDefenseScoreResponsePage showAllDefenseScoreResponsePage = iScoreService.showAllDefenseScore(showAllDefenseScoreRequest);
        return ApiResponse.success(showAllDefenseScoreResponsePage);
    }

    @PostMapping("/checkScore")
    @ApiOperation(value = "查看各项成绩学生总名单", tags = "高级成绩相关功能")
    public ApiResponse<PageUtil<CheckScoreResponse>> checkScore(
            @RequestBody CheckScoreRequest checkScoreRequest
    ) {
        PageUtil<CheckScoreResponse> checkActivityScoreResponsePage = iScoreService.checkScore(checkScoreRequest);
        return ApiResponse.success(checkActivityScoreResponsePage);
    }

    @PostMapping("/checkMyGuideUsualScore")
    @ApiOperation(value = "查看教师指导项目学生平时成绩名单", tags = "高级成绩相关功能")
    public ApiResponse<PageUtil<CheckScoreResponse>> checkMyGuideUsualScore(
            @RequestBody CheckScoreRequest checkScoreRequest
    ) {
        PageUtil<CheckScoreResponse> checkActivityScoreResponsePage = iScoreService.checkMyGuideUsualScore(checkScoreRequest);
        return ApiResponse.success(checkActivityScoreResponsePage);
    }

    @PostMapping("/checkRemainScore")
    @ApiOperation(value = "查看各项成绩未赋分学生总名单", tags = "高级成绩相关功能")
    public ApiResponse<PageUtil<CheckRemainScoreResponse>> checkRemainScore(
            @RequestBody CheckScoreRequest checkScoreRequest
    ) {
        PageUtil<CheckRemainScoreResponse> checkRemainScoreResponsePage = iScoreService.checkRemainScore(checkScoreRequest);
        return ApiResponse.success(checkRemainScoreResponsePage);
    }

    @PostMapping("/checkRemainActivityScore")
    @ApiOperation(value = "查看活动成绩未赋分学生总名单", tags = "高级成绩相关功能")
    public ApiResponse<PageUtil<StudentSignStatisticsResponse>> checkRemainActivityScore(
            @RequestBody CheckScoreRequest checkScoreRequest
    ) {
        PageUtil<StudentSignStatisticsResponse> studentSignStatisticsResponsePage = iScoreService.checkRemainActivityScore(checkScoreRequest);
        return ApiResponse.success(studentSignStatisticsResponsePage);
    }


    @PostMapping("/checkGuideScore")
    @ApiOperation(value = "查看指导项目成绩", tags = "指导老师成绩相关功能")
    public ApiResponse<PageUtil<SearchAllScoreResponse>> checkGuideScore(
            @RequestBody CheckGuideScoreRequest checkGuideScoreRequest
    ) {
        PageUtil<SearchAllScoreResponse> searchAllScoreResponsePage = iScoreService.checkGuideScore(checkGuideScoreRequest);
        return ApiResponse.success(searchAllScoreResponsePage);
    }


}
