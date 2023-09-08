package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.AddInstituteRequest;
import com.Hebut.JiangXin.project.entity.request.ShowAllInstituteRequest;
import com.Hebut.JiangXin.project.entity.response.ShowAllInstituteResponse;
import com.Hebut.JiangXin.project.service.IInstituteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 学院表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/project/institute")
@CrossOrigin
@Api(tags = "学院相关")
public class InstituteController {

    @Resource
    IInstituteService iInstituteService;

    @PostMapping(value = "/addInstitute")
    @ApiOperation(value = "增加学院（测试）", tags = "学院相关功能")
    public ApiResponse addInstitute(
            @RequestBody AddInstituteRequest addInstituteRequest
    ) {
        iInstituteService.addInstitute(addInstituteRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/showAllInstitute")
    @ApiOperation(value = "显示所有学院", tags = "学院相关功能")
    public ApiResponse<PageUtil<ShowAllInstituteResponse>> showAllProject(
            @RequestBody ShowAllInstituteRequest showAllInstituteRequest
    ) {
        PageUtil<ShowAllInstituteResponse> showAllInstituteResponsePageUtil = iInstituteService.showAllInstitute(showAllInstituteRequest);
        return ApiResponse.success(showAllInstituteResponsePageUtil);
    }

}
