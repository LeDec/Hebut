package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.AddMajorRequest;
import com.Hebut.JiangXin.project.entity.request.ShowAllMajorRequest;
import com.Hebut.JiangXin.project.entity.response.ShowAllMajorResponse;
import com.Hebut.JiangXin.project.service.IMajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 专业表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/project/major")
@CrossOrigin
@Api(tags = "专业相关")
public class MajorController {
    @Resource
    IMajorService iMajorService;

    @PostMapping(value = "/addMajor")
    @ApiOperation(value = "增加专业（测试）", tags = "专业相关功能")
    public ApiResponse addInstitute(
            @RequestBody AddMajorRequest addMajorRequest
    ) {
        iMajorService.addMajor(addMajorRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/showAllMajor")
    @ApiOperation(value = "显示所有专业", tags = "专业相关功能")
    public ApiResponse<PageUtil<ShowAllMajorResponse>> showAllMajor(
            @RequestBody ShowAllMajorRequest showAllMajorRequest
    ) {
        PageUtil<ShowAllMajorResponse> showAllMajorResponsePageUtil = iMajorService.showAllMajor(showAllMajorRequest);
        return ApiResponse.success(showAllMajorResponsePageUtil);
    }
}
