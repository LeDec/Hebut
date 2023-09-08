package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.Enum.PathEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckEnrollResponse;
import com.Hebut.JiangXin.project.entity.response.DownloadResponse;
import com.Hebut.JiangXin.project.entity.response.ShowAllApplicantResponse;
import com.Hebut.JiangXin.project.entity.response.ShowAllApplicationResponse;
import com.Hebut.JiangXin.project.service.IApplicationService;
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

/**
 * <p>
 * 报名表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/project/application")
@CrossOrigin
@Api(tags = "报名相关")
public class ApplicationController {

    @Resource
    IApplicationService iApplicationService;

    @PostMapping(value = "/fileApplication")
    @ApiOperation(value = "上传报名文件", tags = "用户报名相关")
    public ApiResponse file(
            @RequestParam("file") MultipartFile application
    ) throws IOException {
        if (application == null || application.getSize() == 0) {
            return ApiResponse.error("请上传文件");
        }
        String fileName = application.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        long time = System.currentTimeMillis();
        String data = "enroll" + time;
        File localPath = new File(PathEnum.FilePath.getMsg() + "img/application/");
        Path path = Paths.get(localPath.getAbsolutePath(), data + suffix);
        application.transferTo(path);
        return ApiResponse.success(data + suffix);
    }

    @PostMapping(value = "/fileApplicationTemplate")
    @ApiOperation(value = "上传报名表模板文件", tags = "用户报名相关")
    public ApiResponse fileApplicationTemplate(
            @RequestParam("file") MultipartFile template
    ) throws IOException {
        if (template == null || template.getSize() == 0) {
            return ApiResponse.error("请上传文件");
        }
        File localPath = new File(PathEnum.FilePath.getMsg() + "template/user");
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

    @PostMapping(value = "/downloadApplicationTemplate")
    @ApiOperation(value = "下载报名表模板文件", tags = "用户报名相关")
    public ApiResponse<DownloadResponse> downloadApplicationTemplate(
            @RequestBody DownloadTemplateRequest downloadTemplateRequest
    ) {
        DownloadResponse downloadResponse = iApplicationService.downloadApplicationTemplate(downloadTemplateRequest);
        return ApiResponse.success(downloadResponse);
    }

    @PostMapping(value = "/enroll")
    @ApiOperation(value = "报名", tags = "用户报名相关")
    public ApiResponse enroll(
            @RequestBody EnrollRequest enrollRequest
    ) {
        iApplicationService.enroll(enrollRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/cancelEnroll")
    @ApiOperation(value = "取消报名", tags = "用户报名相关")
    public ApiResponse cancelEnroll(
            @RequestBody CancelEnrollRequest cancelEnrollRequest
    ) {
        iApplicationService.cancelEnroll(cancelEnrollRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkAuditEnroll")
    @ApiOperation(value = "查看初试审核报名表", tags = "报名相关高级功能")
    public ApiResponse<Page<CheckEnrollResponse>> checkAuditEnroll(
            @RequestBody CheckAuditEnrollRequest checkAuditEnrollRequest
    ) {
        Page<CheckEnrollResponse> checkEnrollResponsePageUtil = iApplicationService.checkAuditEnroll(checkAuditEnrollRequest);
        return ApiResponse.success(checkEnrollResponsePageUtil);
    }

    @PostMapping(value = "/searchAuditEnroll")
    @ApiOperation(value = "搜索初试审核报名表", tags = "报名相关高级功能")
    public ApiResponse<Page<CheckEnrollResponse>> searchAuditEnroll(
            @RequestBody SearchAuditEnrollRequest searchAuditEnrollRequest
    ) {
        Page<CheckEnrollResponse> checkEnrollResponsePageUtil = iApplicationService.searchAuditEnroll(searchAuditEnrollRequest);
        return ApiResponse.success(checkEnrollResponsePageUtil);
    }

    @PostMapping(value = "/auditApplication")
    @ApiOperation(value = "审核初试报名", tags = "报名班主任相关功能")
    public ApiResponse auditApplication(
            @RequestBody AuditApplicationRequest auditApplicationRequest
    ) {
        iApplicationService.auditApplication(auditApplicationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/downloadApplication")
    @ApiOperation(value = "下载报名文件", tags = "报名相关高级功能")
    public ApiResponse<DownloadResponse> downloadApplication(
            @RequestBody DownloadApplicationRequest downloadApplicationRequest
    ) {
        DownloadResponse downloadResponse = iApplicationService.downloadApplication(downloadApplicationRequest);
        return ApiResponse.success(downloadResponse);
    }

    @PostMapping(value = "/checkAuditRetest")
    @ApiOperation(value = "查看复试审核报名表", tags = "报名相关高级功能")
    public ApiResponse<PageUtil<CheckEnrollResponse>> checkAuditRetest(
            @RequestBody CheckAuditEnrollRequest checkAuditEnrollRequest
    ) {
        PageUtil<CheckEnrollResponse> checkEnrollResponsePageUtil = iApplicationService.checkAuditRetest(checkAuditEnrollRequest);
        return ApiResponse.success(checkEnrollResponsePageUtil);
    }

    @PostMapping(value = "/searchAuditRetest")
    @ApiOperation(value = "搜索复试审核报名表", tags = "报名相关高级功能")
    public ApiResponse<PageUtil<CheckEnrollResponse>> searchAuditRetest(
            @RequestBody SearchAuditEnrollRequest searchAuditEnrollRequest
    ) {
        PageUtil<CheckEnrollResponse> checkEnrollResponsePageUtil = iApplicationService.searchAuditRetest(searchAuditEnrollRequest);
        return ApiResponse.success(checkEnrollResponsePageUtil);
    }

    @PostMapping(value = "/auditRetest")
    @ApiOperation(value = "复试审核", tags = "报名相关高级功能")
    public ApiResponse auditRetest(
            @RequestBody AuditApplicationRequest auditApplicationRequest
    ) {
        iApplicationService.auditRetest(auditApplicationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/showAllApplicationProject")
    @ApiOperation(value = "显示所有可报名项目", tags = "用户报名相关")
    public ApiResponse<Page<ShowAllApplicationResponse>> showAllApplicationProject(
            @RequestBody ShowAllApplicationRequest showAllApplicationRequest
    ) {
        Page<ShowAllApplicationResponse> showAllApplicationResponsePage;
        showAllApplicationResponsePage = iApplicationService.showAllApplicationProject(showAllApplicationRequest);
        return ApiResponse.success(showAllApplicationResponsePage);
    }

    @PostMapping(value = "/showAllApplicant")
    @ApiOperation(value = "显示所有已报名成员", tags = "管理员报名相关")
    public ApiResponse<Page<ShowAllApplicantResponse>> showAllApplicant(
            @RequestBody ShowAllApplicantRequest showAllApplicantRequest
    ) {
        Page<ShowAllApplicantResponse> showAllApplicationResponsePage;
        showAllApplicationResponsePage = iApplicationService.showAllApplicant(showAllApplicantRequest);
        return ApiResponse.success(showAllApplicationResponsePage);
    }

}
