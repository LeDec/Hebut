package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.project.entity.request.AddProjectMajorRelationRequest;
import com.Hebut.JiangXin.project.service.IProjectMajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 项目专业关系表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2021-12-29
 */
@RestController
@RequestMapping("/project/major-relation")
@CrossOrigin
@Api(tags = "项目专业相关")
public class ProjectMajorController {
    @Resource
    IProjectMajorService iProjectMajorService;

    @PostMapping(value = "/addMajor")
    @ApiOperation(value = "增加项目专业关系（测试）", tags = "项目专业相关功能")
    public ApiResponse addInstitute(
            @RequestBody AddProjectMajorRelationRequest addProjectMajorRelationRequest
    ) {
        iProjectMajorService.addRelation(addProjectMajorRelationRequest);
        return ApiResponse.success();
    }

}
