package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.service.IProjectUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 项目用户关系表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2021-12-29
 */
@RestController
@RequestMapping("/project/project-user-relation")
@CrossOrigin
@Api(tags = "项目用户相关")
public class ProjectUserController {

    @Resource
    IProjectUserService iProjectUserService;

    @PostMapping("/searchResult")
    @ApiOperation(value = "查询结果", tags = "项目用户关系功能")
    public ApiResponse<SearchResultResponse> searchClassArticle(
            @RequestBody CommonRequest commonRequest
    ) {
        SearchResultResponse searchResultResponse = iProjectUserService.searchResult(commonRequest);
        return ApiResponse.success(searchResultResponse);
    }

    @PostMapping("/searchPartner")
    @ApiOperation(value = "查询小组成员", tags = "项目用户关系功能")
    public ApiResponse<List<SearchPartnerResponse>> searchPartner(
            @RequestBody ScanProjectRequest scanProjectRequest
    ) {
        List<SearchPartnerResponse> searchPartnerResponses = iProjectUserService.searchPartner(scanProjectRequest);
        return ApiResponse.success(searchPartnerResponses);
    }

    @PostMapping("/searchTeacher")
    @ApiOperation(value = "查询指导教师", tags = "项目用户关系功能")
    public ApiResponse<List<InformationResponse>> searchTeacher(
            @RequestBody CommonRequest commonRequest
    ) {
        List<InformationResponse> informationResponses = iProjectUserService.searchTeacher(commonRequest);
        return ApiResponse.success(informationResponses);
    }

    @PostMapping("/searchMyProject")
    @ApiOperation(value = "查询我参加的项目", tags = "项目用户关系功能")
    public ApiResponse<PageUtil<ShowProjectResponse>> searchMyProject(
            @RequestBody SearchMyProjectRequest searchMyProjectRequest
    ) {
        PageUtil<ShowProjectResponse> showProjectResponses = iProjectUserService.searchMyProject(searchMyProjectRequest);
        return ApiResponse.success(showProjectResponses);
    }

    @PostMapping("/localProject")
    @ApiOperation(value = "我已参与的匠心项目", tags = "项目用户关系功能")
    public ApiResponse<ScanProjectResponse> localProject(
            @RequestBody CommonRequest commonRequest
    ) {
        ScanProjectResponse scanProjectResponse = iProjectUserService.localProject(commonRequest);
        return ApiResponse.success(scanProjectResponse);
    }

    @PostMapping("/futureProject")
    @ApiOperation(value = "我已报名的匠心项目", tags = "项目用户关系功能")
    public ApiResponse<ScanProjectResponse> futureProject(
            @RequestBody CommonRequest commonRequest
    ) {
        ScanProjectResponse scanProjectResponse = iProjectUserService.futureProject(commonRequest);
        return ApiResponse.success(scanProjectResponse);
    }

    @PostMapping("/searchGuideProject")
    @ApiOperation(value = "查询教师指导的项目", tags = "项目用户关系功能")
    public ApiResponse<PageUtil<SearchGuideProjectResponse>> searchGuideProject(
            @RequestBody SearchGuideProjectRequest searchGuideProjectRequest
    ) {
        PageUtil<SearchGuideProjectResponse> showProjectResponses = iProjectUserService.searchGuideProject(searchGuideProjectRequest);
        return ApiResponse.success(showProjectResponses);
    }


    @PostMapping("/distributeProject")
    @ApiOperation(value = "分配专家项目审核任务", tags = "项目用户关系管理员相关功能")
    public ApiResponse distributeProject(
            @RequestBody DistributeProjectRequest distributeProjectRequest
    ) {
        iProjectUserService.distributeProject(distributeProjectRequest);
        return ApiResponse.success();
    }

}
