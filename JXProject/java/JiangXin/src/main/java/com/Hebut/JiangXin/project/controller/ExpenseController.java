package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.Enum.PathEnum;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.service.IExpenseService;
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
 * 报销表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2021-12-31
 */
@RestController
@RequestMapping("/project/expense")
@CrossOrigin
@Api(tags = "报销相关")
public class ExpenseController {

    @Resource
    IExpenseService iExpenseService;

    @PostMapping(value = "/fileCertificate")
    @ApiOperation(value = "上传凭证")
    public ApiResponse file(
            @RequestParam("file") MultipartFile certificate
    ) throws IOException {
        if (certificate == null || certificate.getSize() == 0) {
            return ApiResponse.error("请上传文件");
        }
        String fileName = certificate.getOriginalFilename();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        long time = System.currentTimeMillis();
        String data = "expense" + time;
        File localPath = new File(PathEnum.FilePath.getMsg() + "img/certificate/");
        Path path = Paths.get(localPath.getAbsolutePath(), data + suffix);
        certificate.transferTo(path);
        return ApiResponse.success(data + suffix);
    }

    @PostMapping(value = "/addExpense")
    @ApiOperation(value = "增加报销")
    public ApiResponse addExpense(
            @RequestBody AddExpenseRequest addExpenseRequest
    ) {
        iExpenseService.addExpense(addExpenseRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkExpense")
    @ApiOperation(value = "查看报销")
    public ApiResponse<PageUtil<CheckExpenseResponse>> checkExpense(
            @RequestBody CheckExpenseRequest checkExpenseRequest
    ) {
        PageUtil<CheckExpenseResponse> checkExpenseResponsePage = iExpenseService.checkExpense(checkExpenseRequest);
        return ApiResponse.success(checkExpenseResponsePage);
    }

    @PostMapping(value = "/cancelExpense")
    @ApiOperation(value = "撤销报销")
    public ApiResponse cancelExpense(
            @RequestBody CancelExpenseRequest cancelExpenseRequest
    ) {
        iExpenseService.cancelExpense(cancelExpenseRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/searchExpense")
    @ApiOperation(value = "搜索本人报销")
    public ApiResponse<PageUtil<CheckExpenseResponse>> searchExpense(
            @RequestBody SearchExpenseRequest searchExpenseRequest
    ) {
        PageUtil<CheckExpenseResponse> checkExpenseResponsePage = iExpenseService.searchExpense(searchExpenseRequest);
        return ApiResponse.success(checkExpenseResponsePage);
    }

    @PostMapping(value = "/searchProjectExpense")
    @ApiOperation(value = "搜索项目报销")
    public ApiResponse<PageUtil<CheckExpenseResponse>> searchProjectExpense(
            @RequestBody SearchProjectExpenseRequest searchProjectExpenseRequest
    ) {
        PageUtil<CheckExpenseResponse> checkExpenseResponsePage = iExpenseService.searchProjectExpense(searchProjectExpenseRequest);
        return ApiResponse.success(checkExpenseResponsePage);
    }

    @PostMapping(value = "/inquireExpense")
    @ApiOperation(value = "查询报销金额")
    public ApiResponse<InquireExpenseResponse> inquireExpense(
            @RequestBody CommonRequest commonRequest
    ) {
        InquireExpenseResponse inquireExpenseResponse = iExpenseService.inquireExpense(commonRequest);
        return ApiResponse.success(inquireExpenseResponse);
    }

    @PostMapping(value = "/inquireUserExpense")
    @ApiOperation(value = "查询用户报销金额", tags = "管理员报销相关功能")
    public ApiResponse<InquireExpenseResponse> inquireUserExpense(
            @RequestBody InquireUserExpenseRequest inquireUserExpenseRequest
    ) {
        InquireExpenseResponse inquireExpenseResponse = iExpenseService.inquireUserExpense(inquireUserExpenseRequest);
        return ApiResponse.success(inquireExpenseResponse);
    }

    @PostMapping(value = "/downloadExpense")
    @ApiOperation(value = "下载报销凭证")
    public ApiResponse<DownloadResponse> downloadExpense(
            @RequestBody DownloadExpenseRequest downloadExpenseRequest
    ) {
        DownloadResponse downloadResponse = iExpenseService.downloadExpense(downloadExpenseRequest);
        return ApiResponse.success(downloadResponse);
    }

    @PostMapping(value = "/scanExpense")
    @ApiOperation(value = "查看项目报销表", tags = "报销相关管理员功能")
    public ApiResponse<PageUtil<ScanExpenseResponse>> scanExpense(
            @RequestBody SearchAuditExpenseRequest searchAuditExpenseRequest
    ) {
        PageUtil<ScanExpenseResponse> searchAuditExpenseResponsePage = iExpenseService.scanExpense(searchAuditExpenseRequest);
        return ApiResponse.success(searchAuditExpenseResponsePage);
    }

    @PostMapping(value = "/checkAuditExpense")
    @ApiOperation(value = "查看总审核报销表", tags = "报销相关管理员功能")
    public ApiResponse<PageUtil<SearchAuditExpenseResponse>> checkAuditExpense(
            @RequestBody CheckRequest checkRequest
    ) {
        PageUtil<SearchAuditExpenseResponse> searchAuditExpenseResponsePage = iExpenseService.checkAuditExpense(checkRequest);
        return ApiResponse.success(searchAuditExpenseResponsePage);
    }

    @PostMapping(value = "/auditExpense")
    @ApiOperation(value = "审核报销", tags = "报销相关老师功能")
    public ApiResponse auditExpense(
            @RequestBody AuditExpenseRequest auditExpenseRequest
    ) {
        iExpenseService.auditExpense(auditExpenseRequest);
        return ApiResponse.success();
    }


}
