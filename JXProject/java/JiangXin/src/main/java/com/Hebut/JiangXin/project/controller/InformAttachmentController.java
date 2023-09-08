package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.Enum.PathEnum;
import com.Hebut.JiangXin.project.entity.request.DownloadAttachmentRequest;
import com.Hebut.JiangXin.project.entity.response.DownloadResponse;
import com.Hebut.JiangXin.project.entity.response.fileResponse;
import com.Hebut.JiangXin.project.service.IInformAttachmentService;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.webresources.FileResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 通知附件表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/project/inform-attachment")
@CrossOrigin
public class InformAttachmentController {
    @Resource
    IInformAttachmentService iInformAttachmentService;

    @PostMapping(value = "/fileAttachment")
    @ApiOperation(value = "上传附件", tags = "通知相关")
    public ApiResponse<List<fileResponse>> file(
            @RequestParam("file") MultipartFile[] certificates
    ) throws IOException {
        List<fileResponse> fileResponses = new ArrayList<>();
        for (MultipartFile certificate : certificates) {
            if (certificate == null || certificate.getSize() == 0) {
                return ApiResponse.error(ErrorEnum.NOT_FOUND_ERROR.getCode(), "请上传文件");
            }
            fileResponse fileResponse = new fileResponse();
            String fileName = certificate.getOriginalFilename();
            assert fileName != null;
            String suffix = fileName.substring(fileName.lastIndexOf('.'));
            long time = System.currentTimeMillis();
            String data = "attachment" + time;
            File localPath = new File(PathEnum.FilePath.getMsg() + "img/attachment/");
            Path path = Paths.get(localPath.getAbsolutePath(), data + suffix);
            certificate.transferTo(path);
            fileResponse.setFileName(data + suffix);
            fileResponse.setLocation(PathEnum.URl.getMsg() + fileResponse.getFileName());
            fileResponses.add(fileResponse);
        }
        return ApiResponse.success(fileResponses);
    }


    @PostMapping(value = "/downAttachment")
    @ApiOperation(value = "下载通知附件", tags = "通知附件相关功能")
    public ApiResponse<DownloadResponse> downAttachment(
            @RequestBody DownloadAttachmentRequest downloadAttachmentRequest
    ) {
        DownloadResponse downloadResponse = iInformAttachmentService.downAttachment(downloadAttachmentRequest);
        return ApiResponse.success(downloadResponse);
    }

}
