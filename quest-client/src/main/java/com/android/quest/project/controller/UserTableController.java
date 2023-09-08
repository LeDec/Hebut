package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.*;
import com.android.quest.project.entity.response.CheckUserListResponse;
import com.android.quest.project.entity.response.LoginResponse;
import com.android.quest.project.entity.response.MyInformationResponse;
import com.android.quest.project.service.IUserTableService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@RestController
@RequestMapping("/project/user-table")
@CrossOrigin
public class UserTableController {

    @Resource
    IUserTableService iUserTableService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录", tags = "登录注册功能")
    public ApiResponse<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse loginResponse = iUserTableService.login(loginRequest);
        return ApiResponse.success(loginResponse);
    }

    @PostMapping(value = "/register")
    @ApiOperation(value = "注册", tags = "登录注册功能")
    public ApiResponse<LoginResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        LoginResponse loginResponse = iUserTableService.register(registerRequest);
        return ApiResponse.success(loginResponse);
    }

    @PostMapping(value = "/forgetPassword")
    @ApiOperation(value = "忘记密码", tags = "登录注册功能")
    public ApiResponse<LoginResponse> forgetPassword(
            @RequestBody ForgetPasswordRequest forgetPasswordRequest
    ) {
        LoginResponse loginResponse = iUserTableService.forgetPassword(forgetPasswordRequest);
        return ApiResponse.success(loginResponse);
    }

    @PostMapping(value = "/myInformation")
    @ApiOperation(value = "我的信息", tags = "个人信息功能")
    public ApiResponse<MyInformationResponse> myInformation(
            @RequestBody MyInformationRequest myInformationRequest
    ) {
        MyInformationResponse myInformationResponse = iUserTableService.myInformation(myInformationRequest);
        return ApiResponse.success(myInformationResponse);
    }

    @PostMapping(value = "/changeInformation")
    @ApiOperation(value = "修改个人信息", tags = "个人信息功能")
    public ApiResponse changeInformation(
            @RequestBody ChangeInformationRequest changeInformationRequest
    ) {
        iUserTableService.changeInformation(changeInformationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/changeProfile")
    @ApiOperation(value = "修改头像信息", tags = "个人信息功能")
    public ApiResponse changeProfile(
            @RequestBody ChangeProfileRequest changeProfileRequest
    ) {
        iUserTableService.changeProfile(changeProfileRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkUserList")
    @ApiOperation(value = "查看所有用户", tags = "web端接口")
    public ApiResponse<CheckUserListResponse> checkUserList(
            @RequestBody CheckUserListRequest checkUserListRequest
    ) {
        CheckUserListResponse checkUserListResponse = iUserTableService.checkUserList(checkUserListRequest);
        return ApiResponse.success(checkUserListResponse);
    }

}
