package com.Hebut.JiangXin.project.controller;

import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.project.entity.request.MailRequest;
import com.Hebut.JiangXin.project.entity.request.ResetPasswordRequest;
import com.Hebut.JiangXin.project.entity.response.MailResponse;
import com.Hebut.JiangXin.project.service.impl.MailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author LiDong
 */
@CrossOrigin
@RestController
public class EmailController {

    @Resource
    MailService mailService;


    @PostMapping(value = "/sendEMail")
    @ApiOperation(value = "发送邮件", tags = "邮件相关功能")
    public ApiResponse<MailResponse> sendMail(
            @RequestBody MailRequest mailRequest
    ) {
        MailResponse mailResponse = new MailResponse();
        mailResponse.setCode(mailService.sendSimpleMail(mailRequest.getUserId(), "匠心系统重置密码邮箱验证码"));
        mailResponse.setUserId(mailRequest.getUserId());
        return ApiResponse.success(mailResponse);
    }

    @PostMapping(value = "/resetPassword")
    @ApiOperation(value = "重置密码", tags = "邮件相关功能")
    public ApiResponse resetPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest
    ) {
        mailService.resetPassword(resetPasswordRequest);
        return ApiResponse.success("密码已经修改为: hebut + 您的学号 。");
    }
}
