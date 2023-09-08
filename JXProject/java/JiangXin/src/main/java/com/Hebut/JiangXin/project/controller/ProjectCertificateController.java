package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.Enum.PathEnum;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckAllMaterialPageResponse;
import com.Hebut.JiangXin.project.entity.response.CheckMaterialResponse;
import com.Hebut.JiangXin.project.entity.response.CheckMaterialStageResponse;
import com.Hebut.JiangXin.project.entity.response.DownloadResponse;
import com.Hebut.JiangXin.project.service.IProjectCertificateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p>
 * 项目报告上传关系表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/project/certificate-relation")
@CrossOrigin
@Api(tags = "提交资料相关")
public class ProjectCertificateController {

    @Resource
    IProjectCertificateService iProjectCertificateService;


    @PostMapping(value = "/fileMaterial")
    @ApiOperation(value = "上传项目资料")
    public ApiResponse fileMaterial(
            @RequestParam("file") MultipartFile application
    ) throws IOException {
        if (application == null || application.getSize() == 0) {
            return ApiResponse.error("请上传文件");
        }
        String fileName = application.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        long time = System.currentTimeMillis();
        String data = name + "-" + time;
        File localPath = new File(PathEnum.FilePath.getMsg() + "img/material");
        Path path = Paths.get(localPath.getAbsolutePath(), data + suffix);
        application.transferTo(path);
        return ApiResponse.success(data + suffix);
    }

    @PostMapping(value = "/submitMaterial")
    @ApiOperation(value = "提交资料")
    public ApiResponse submitMaterial(
            @RequestBody MaterialRequest materialRequest
    ) {
        iProjectCertificateService.submitMaterial(materialRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/deleteMaterial")
    @ApiOperation(value = "删除提交资料")
    public ApiResponse deleteMaterial(
            @RequestBody DeleteMaterialRequest deleteMaterialRequest
    ) {
        iProjectCertificateService.deleteMaterial(deleteMaterialRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/downloadMaterial")
    @ApiOperation(value = "下载项目资料", tags = "项目列表功能")
    public ApiResponse<DownloadResponse> downloadMaterial(
            @RequestBody DownloadMaterialRequest downloadMaterialRequest
    ) {
        DownloadResponse downloadResponse = iProjectCertificateService.downloadMaterial(downloadMaterialRequest);
        return ApiResponse.success(downloadResponse);
    }

    @PostMapping(value = "/checkMaterialStage")
    @ApiOperation(value = "查看提交资料状态", tags = "项目列表功能")
    public ApiResponse<CheckMaterialStageResponse> checkMaterialStage(
            @RequestBody CheckMaterialStageRequest checkMaterialStageRequest
    ) {
        CheckMaterialStageResponse checkMaterialStageResponse = iProjectCertificateService.checkMaterialStage(checkMaterialStageRequest);
        return ApiResponse.success(checkMaterialStageResponse);
    }

    @PostMapping(value = "/checkMaterial")
    @ApiOperation(value = "查看项目已提交资料", tags = "项目列表功能")
    public ApiResponse<PageUtil<CheckMaterialResponse>> checkMaterial(
            @RequestBody CheckMaterialRequest checkMaterialStageRequest
    ) {
        PageUtil<CheckMaterialResponse> checkMaterialResponsePage = iProjectCertificateService.checkMaterial(checkMaterialStageRequest);
        return ApiResponse.success(checkMaterialResponsePage);
    }

    @PostMapping(value = "/auditMaterial")
    @ApiOperation(value = "审核资料")
    public ApiResponse auditMaterial(
            @RequestBody AuditMaterialRequest auditMaterialRequest
    ) {
        iProjectCertificateService.auditMaterial(auditMaterialRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkAllMaterial")
    @ApiOperation(value = "查看项目所有已提交资料", tags = "项目列表功能")
    public ApiResponse<CheckAllMaterialPageResponse> checkAllMaterial(
            @RequestBody CheckAllMaterialRequest checkAllMaterialRequest
    ) {
        CheckAllMaterialPageResponse checkAllMaterialPageResponse = iProjectCertificateService.checkAllMaterial(checkAllMaterialRequest);
        return ApiResponse.success(checkAllMaterialPageResponse);
    }

}
