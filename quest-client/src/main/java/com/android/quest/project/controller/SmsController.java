package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.project.entity.request.SmsRequest;
import com.android.quest.project.entity.response.SmsResponse;
import com.android.quest.project.service.impl.AliyunSendSmsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/project/message")
@CrossOrigin
public class SmsController {
    @Resource
    AliyunSendSmsService aliyunSendSmsService;

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;

    @PostMapping(value = "/send")
    @ApiOperation(value = "发送验证码", tags = "登录注册功能")
    public ApiResponse<SmsResponse> send(
            @RequestBody SmsRequest smsRequest
    ) {
        SmsResponse smsResponse = new SmsResponse();

        // 如果redis 中根据手机号拿不到验证码，则生成4位随机验证码
        String code = UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 6);
        smsResponse.setVerify_code(code);

        // 验证码存入codeMap
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("code", code);

        // 调用aliyunSendSmsService发送短信
        Boolean bool = aliyunSendSmsService.sendMessage(smsRequest.getPhone(), templateCode, codeMap);
        if (bool) {
            return ApiResponse.success(smsResponse);
        } else {
            throw new CustomException(ErrorEnum.VERIFIED_WRONG.getCode(), "验证码发送错误！");
        }
    }
}
