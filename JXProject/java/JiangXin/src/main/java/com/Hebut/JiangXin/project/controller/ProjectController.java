package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.Enum.PathEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.service.IProjectService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * <p>
 * 项目表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2021-12-07
 */
@RestController
@RequestMapping("/project/project")
@CrossOrigin
@Api(tags = "项目相关")
public class ProjectController {


    @Resource
    IProjectService iProjectService;

    @PostMapping(value = "/fileProspectus")
    @ApiOperation(value = "上传项目书", tags = "项目列表功能")
    public ApiResponse file(
            @RequestParam("file") MultipartFile application
    ) throws IOException {
        if (application == null || application.getSize() == 0) {
            return ApiResponse.error("请上传文件");
        }
        String fileName = UUID.randomUUID().toString().replace("\\-", "") + application.getOriginalFilename();
        assert fileName != null;
        File localPath = new File(PathEnum.FilePath.getMsg() + "prospectus");
        Path path = Paths.get(localPath.getAbsolutePath(), fileName);
        application.transferTo(path);
        return ApiResponse.success(fileName);
    }

    @PostMapping(value = "/fileProspectusTemplate")
    @ApiOperation(value = "上传项目书模板", tags = "项目相关功能")
    public ApiResponse fileTemplate(
            @RequestParam("file") MultipartFile template
    ) throws IOException {
        if (template == null || template.getSize() == 0) {
            return ApiResponse.error("请上传文件");
        }
        File localPath = new File(PathEnum.FilePath.getMsg() + "template/project");
        File[] array = localPath.listFiles();
        if (array.length > 0) {
            if (array[0].delete()) {
            } else {
                throw new CustomException(ErrorEnum.FILE_DELETE_ERROR.getCode(), "文件删除失败");
            }
        }
        String suffix = template.getOriginalFilename().substring(template.getOriginalFilename().lastIndexOf("."));
        String fileName = "template" + suffix;
        assert fileName != null;
        Path path = Paths.get(localPath.getAbsolutePath(), fileName);
        template.transferTo(path);
        return ApiResponse.success();
    }

    @PostMapping(value = "/downloadProspectusTemplate")
    @ApiOperation(value = "下载项目计划书模板", tags = "项目相关功能")
    public ApiResponse<DownloadResponse> downloadTemplate(
            @RequestBody DownloadTemplateRequest downloadTemplateRequest
    ) {
        DownloadResponse downloadResponse = iProjectService.downloadTemplate(downloadTemplateRequest);
        return ApiResponse.success(downloadResponse);
    }

    @PostMapping(value = "/addProject")
    @ApiOperation(value = "增加项目", tags = "项目列表功能")
    public ApiResponse addProject(
            @RequestBody AddProjectRequest addProjectRequest
    ) {
        iProjectService.addProject(addProjectRequest);
        return ApiResponse.success();
    }


    @PostMapping(value = "/addProjectDesignation")
    @ApiOperation(value = "分配项目编号", tags = "项目列表功能")
    public ApiResponse addProjectDesignation(
            @RequestBody AddProjectDesignationRequest addProjectDesignationRequest
    ) {
        iProjectService.addProjectDesignation(addProjectDesignationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/showAllProject")
    @ApiOperation(value = "显示所有项目", tags = "项目列表功能")
    public ApiResponse<ShowProjectResponsePage> showAllProject(
            @RequestBody ShowProjectRequest showProjectRequest
    ) {
        ShowProjectResponsePage showProjectResponsePage = iProjectService.showAllProject(showProjectRequest);
        return ApiResponse.success(showProjectResponsePage);
    }

    @PostMapping(value = "/showAllOtherProject")
    @ApiOperation(value = "显示非匠心项目", tags = "项目列表功能")
    public ApiResponse<Page<ShowAllOtherProjectResponse>> showAllOtherProject(
            @RequestBody ShowAllOtherProjectRequest showAllOtherProjectRequest
    ) {
        Page<ShowAllOtherProjectResponse> showAllOtherProjectResponsePage;
        showAllOtherProjectResponsePage = iProjectService.showAllOtherProject(showAllOtherProjectRequest);
        return ApiResponse.success(showAllOtherProjectResponsePage);
    }

    @PostMapping(value = "/showAuditProject")
    @ApiOperation(value = "显示初试待审核项目", tags = "项目列表功能")
    public ApiResponse<PageUtil<ShowAuditProjectResponse>> showAuditProject(
            @RequestBody ShowAuditProjectRequest showAuditProjectRequest
    ) {
        PageUtil<ShowAuditProjectResponse> showAuditProjectResponsePage;
        showAuditProjectResponsePage = iProjectService.showAuditProject(showAuditProjectRequest);
        return ApiResponse.success(showAuditProjectResponsePage);
    }

    @PostMapping(value = "/searchProject")
    @ApiOperation(value = "搜索项目", tags = "项目列表功能")
    public ApiResponse<PageUtil<ShowProjectResponse>> searchProject(
            @RequestBody SearchProjectRequest searchProjectRequest
    ) {
        PageUtil<ShowProjectResponse> showProjectResponsePage = iProjectService.searchProject(searchProjectRequest);
        return ApiResponse.success(showProjectResponsePage);
    }

    @PostMapping(value = "/scanProject")
    @ApiOperation(value = "查看项目内容", tags = "项目列表功能")
    public ApiResponse<ScanProjectResponse> scanProject(
            @RequestBody ScanProjectRequest scanProjectRequest
    ) {
        ScanProjectResponse scanProjectResponse = iProjectService.scanProject(scanProjectRequest);
        return ApiResponse.success(scanProjectResponse);
    }

    @PostMapping(value = "/downloadProspectus")
    @ApiOperation(value = "下载项目计划书", tags = "项目列表功能")
    public ApiResponse<DownloadResponse> downloadProspectus(
            @RequestBody DownloadProspectusRequest downloadProspectusRequest
    ) {
        DownloadResponse downloadResponse = iProjectService.downloadProspectus(downloadProspectusRequest);
        return ApiResponse.success(downloadResponse);
    }


    @PostMapping(value = "/auditProject")
    @ApiOperation(value = "审核项目", tags = "管理员项目相关功能")
    public ApiResponse auditProject(
            @RequestBody AuditProjectRequest auditProjectRequest
    ) {
        iProjectService.auditProject(auditProjectRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/deleteProject")
    @ApiOperation(value = "删除项目", tags = "管理员项目相关功能")
    public ApiResponse deleteProject(
            @RequestBody DeleteProjectRequest deleteProjectRequest
    ) {
        iProjectService.deleteProject(deleteProjectRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/showPassProject")
    @ApiOperation(value = "显示初试已通过项目", tags = "项目列表功能")
    public ApiResponse<PageUtil<ShowPassProjectResponse>> showPassProject(
            @RequestBody ShowPassProjectRequest showPassProjectRequest
    ) {
        PageUtil<ShowPassProjectResponse> showPassProjectResponsePage;
        showPassProjectResponsePage = iProjectService.showPassProject(showPassProjectRequest);
        return ApiResponse.success(showPassProjectResponsePage);
    }

    @PostMapping(value = "/processProject")
    @ApiOperation(value = "项目阶段进行", tags = "指导教师项目相关功能")
    public ApiResponse processProject(
            @RequestBody ProcessProjectRequest processProjectRequest
    ) {
        iProjectService.processProject(processProjectRequest);
        return ApiResponse.success();
    }


}
