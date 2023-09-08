package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.AdminLoginRequest;
import com.android.quest.project.entity.request.LoginRequest;
import com.android.quest.project.entity.response.LoginResponse;
import com.android.quest.project.service.IAdminTableService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liDong
 * @since 2023-04-28
 */
@RestController
@RequestMapping("/project/admin-table")
@CrossOrigin
public class AdminTableController {

    @Resource
    IAdminTableService iAdminTableService;

    @PostMapping(value = "/adminLogin")
    @ApiOperation(value = "管理员登录", tags = "web端接口")
    public ApiResponse<LoginResponse> adminLogin(
            @RequestBody AdminLoginRequest adminLoginRequest
    ) {
        LoginResponse loginResponse = iAdminTableService.adminLogin(adminLoginRequest);
        return ApiResponse.success(loginResponse);
    }

}
