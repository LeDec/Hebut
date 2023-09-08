package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.project.entity.request.AddInstituteMajorRelationRequest;
import com.Hebut.JiangXin.project.entity.request.ShowMajorRequest;
import com.Hebut.JiangXin.project.entity.response.ShowMajorResponse;
import com.Hebut.JiangXin.project.service.IInstituteMajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 学院专业关系表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/project/institute-major-relation")
@CrossOrigin
@Api(tags = "学院专业相关")
public class InstituteMajorController {

    @Resource
    IInstituteMajorService iInstituteMajorService;

    @PostMapping(value = "/addIMrelation")
    @ApiOperation(value = "增加学院专业关系（测试）", tags = "学院专业关系相关功能")
    public ApiResponse addInstitute(
            @RequestBody AddInstituteMajorRelationRequest addInstituteMajorRelation
    ) {
        iInstituteMajorService.addRelation(addInstituteMajorRelation);
        return ApiResponse.success();
    }

    @PostMapping(value = "/showMajor")
    @ApiOperation(value = "显示该学院下的专业", tags = "学院专业关系相关功能")
    public ApiResponse<List<ShowMajorResponse>> showMajor(
            @RequestBody ShowMajorRequest ShowMajorRequest
    ) {
        List<ShowMajorResponse> showMajorResponseList = iInstituteMajorService.showMajor(ShowMajorRequest);
        return ApiResponse.success(showMajorResponseList);
    }
}
