package com.Hebut.JiangXin.project.controller;


import com.Hebut.JiangXin.common.ApiResponse;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.service.IUserService;
import com.baomidou.kaptcha.Kaptcha;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/project/user")
@CrossOrigin
@Api(tags = "用户相关")
public class UserController {
    @Resource
    IUserService iUserService;

    @Resource
    private Kaptcha kaptcha;

    @GetMapping("/render")
    @ApiOperation(value = "获取图形验证码", tags = "验证码")
    public JSONObject render() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            String code = kaptcha.render();
            if (null != code && !"".equals(code)) {
                jsonObject.put("success", true);
                jsonObject.put("message", "验证渲染成功！");
                jsonObject.put("data", code);
            } else {
                jsonObject.put("success", false);
                jsonObject.put("message", "验证渲染失败！");
                jsonObject.put("data", "");
            }
        } catch (Exception e) {
            jsonObject.put("success", false);
            jsonObject.put("message", "验证渲染失败：" + e.getMessage());
            jsonObject.put("data", "");
        }
        return jsonObject;
    }


    @PostMapping("/register")
    @ApiOperation(value = "学生注册", tags = "登录注册功能")
    public ApiResponse register(
            @RequestBody @Valid RegisterRequest registerRequest
    ) {
        iUserService.register(registerRequest);
        return ApiResponse.success();
    }

    @PostMapping("/registerTeacher")
    @ApiOperation(value = "教师注册", tags = "登录注册功能")
    public ApiResponse registerTeacher(
            @RequestBody RegisterTeacherRequest registerTeacherRequest
    ) {
        iUserService.registerTeacher(registerTeacherRequest);
        return ApiResponse.success();
    }

    @PostMapping("/checkRegisterTeacher")
    @ApiOperation(value = "查看待审核教师注册", tags = "登录注册功能")
    public ApiResponse<Page<TeacherAuditResponse>> checkRegisterTeacher(
            @RequestBody CheckRequest checkRequest
    ) {
        Page<TeacherAuditResponse> teacherAuditResponsePage = iUserService.checkRegisterTeacher(checkRequest);
        return ApiResponse.success(teacherAuditResponsePage);
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "账号登录", tags = "登录注册功能")
    public ApiResponse<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {
        LoginResponse loginResponse;
        loginResponse = iUserService.login(loginRequest);
        return ApiResponse.success(loginResponse);
    }

    @PostMapping(value = "/Information")
    @ApiOperation(value = "查询信息", tags = "用户个人功能")
    public ApiResponse<InformationResponse> information(
            @RequestBody CommonRequest commonRequest
    ) {
        InformationResponse informationResponse;
        informationResponse = iUserService.information(commonRequest);
        return ApiResponse.success(informationResponse);
    }

    @PostMapping(value = "/changeInformation")
    @ApiOperation(value = "修改个人信息", tags = "用户个人功能")
    public ApiResponse changeInformation(
            @RequestBody ChangeInformationRequest changeInformationRequest
    ) {
        iUserService.changeInformation(changeInformationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/changePassword")
    @ApiOperation(value = "修改密码", tags = "用户个人功能")
    public ApiResponse changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        iUserService.changePassword(changePasswordRequest);
        return ApiResponse.success();
    }


    @PostMapping(value = "/changeUserType")
    @ApiOperation(value = "修改用户类型", tags = "高级管理员功能")
    public ApiResponse changeUserType(
            @RequestBody ChangeUserTypeRequest changeUserTypeRequest
    ) {
        iUserService.changeUserType(changeUserTypeRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/changeTeacherType")
    @ApiOperation(value = "修改教师用户类型", tags = "管理员用户相关功能")
    public ApiResponse changeTeacherType(
            @RequestBody ChangeUserTypeRequest changeUserTypeRequest
    ) {
        iUserService.changeTeacherType(changeUserTypeRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/checkStudent")
    @ApiOperation(value = "查看学生用户信息", tags = "管理员用户相关功能")
    public ApiResponse<Page<CheckStudentResponse>> checkStudent(
            @RequestBody CheckStudentRequest checkStudentRequest
    ) {
        Page<CheckStudentResponse> checkStudentRequestPage = iUserService.checkStudent(checkStudentRequest);
        return ApiResponse.success(checkStudentRequestPage);
    }

    @PostMapping(value = "/checkUserExperience")
    @ApiOperation(value = "查看用户项目经历", tags = "普通用户相关功能")
    public ApiResponse<PageUtil<CheckUserExperienceResponse>> checkUserExperience(
            @RequestBody CheckUserExperienceRequest checkUserExperienceRequest
    ) {
        PageUtil<CheckUserExperienceResponse> checkUserExperienceResponsePage = iUserService.checkUserExperience(checkUserExperienceRequest);
        return ApiResponse.success(checkUserExperienceResponsePage);
    }

    @PostMapping(value = "/checkClassmates")
    @ApiOperation(value = "查看匠心班学生信息", tags = "班主任用户相关功能")
    public ApiResponse<Page<CheckClassmatesResponse>> checkClassmates(
            @RequestBody CheckUserRequest checkUserRequest
    ) {
        Page<CheckClassmatesResponse> checkClassmatesResponsePage = iUserService.checkClassmates(checkUserRequest);
        return ApiResponse.success(checkClassmatesResponsePage);
    }

    @PostMapping(value = "/searchClassmates")
    @ApiOperation(value = "搜索匠心班学生信息", tags = "班主任用户相关功能")
    public ApiResponse<PageUtil<CheckClassmatesResponse>> searchClassmates(
            @RequestBody SearchClassmatesRequest searchClassmatesRequest
    ) {
        PageUtil<CheckClassmatesResponse> checkClassmatesResponsePage = iUserService.searchClassmates(searchClassmatesRequest);
        return ApiResponse.success(checkClassmatesResponsePage);
    }

    @PostMapping(value = "/checkTeacher")
    @ApiOperation(value = "查看教师用户信息", tags = "管理员用户相关功能")
    public ApiResponse<Page<TeacherInformationResponse>> checkTeacher(
            @RequestBody CheckUserRequest checkUserRequest
    ) {
        Page<TeacherInformationResponse> teacherInformationResponsePage = iUserService.checkTeacher(checkUserRequest);
        return ApiResponse.success(teacherInformationResponsePage);
    }

    @PostMapping(value = "/searchStudent")
    @ApiOperation(value = "搜索学生用户信息", tags = "管理员用户相关功能")
    public ApiResponse<PageUtil<CheckStudentResponse>> searchStudent(
            @RequestBody SearchStudentRequest searchStudentRequest
    ) {
        PageUtil<CheckStudentResponse> checkStudentResponsePage = iUserService.searchStudent(searchStudentRequest);
        return ApiResponse.success(checkStudentResponsePage);
    }

    @PostMapping(value = "/searchTeacher")
    @ApiOperation(value = "搜索教师用户信息", tags = "管理员用户相关功能")
    public ApiResponse<PageUtil<TeacherInformationResponse>> searchTeacher(
            @RequestBody SearchTeacherRequest searchTeacherRequest
    ) {
        PageUtil<TeacherInformationResponse> teacherInformationResponsePage = iUserService.searchTeacher(searchTeacherRequest);
        return ApiResponse.success(teacherInformationResponsePage);
    }

    @PostMapping(value = "/checkAuditTeacher")
    @ApiOperation(value = "搜索待审核教师信息", tags = "管理员用户相关功能")
    public ApiResponse<PageUtil<TeacherInformationResponse>> checkAuditTeacher(
            @RequestBody CheckAuditTeacherRequest checkAuditTeacherRequest
    ) {
        PageUtil<TeacherInformationResponse> teacherInformationResponsePage = iUserService.checkAuditTeacher(checkAuditTeacherRequest);
        return ApiResponse.success(teacherInformationResponsePage);
    }

    @PostMapping(value = "/auditRegister")
    @ApiOperation(value = "审核教师注册", tags = "管理员用户相关功能")
    public ApiResponse auditRegister(
            @RequestBody AuditTeacherRequest auditTeacherRequest
    ) {
        iUserService.auditRegister(auditTeacherRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/addTeacher")
    @ApiOperation(value = "增加教师类用户", tags = "管理员用户相关功能")
    public ApiResponse addTeacher(
            @RequestBody AddTeacherRequest addTeacherRequest
    ) {
        iUserService.addTeacher(addTeacherRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/deleteUser")
    @ApiOperation(value = "删除教师用户", tags = "管理员用户相关功能")
    public ApiResponse deleteUser(
            @RequestBody DeleteUserRequest deleteUserRequest
    ) {
        iUserService.deleteUser(deleteUserRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/changeTeacher")
    @ApiOperation(value = "编辑教师用户信息", tags = "管理员用户相关功能")
    public ApiResponse changeTeacher(
            @RequestBody ChangeTeacherInformationRequest changeTeacherInformationRequest
    ) {
        iUserService.changeTeacher(changeTeacherInformationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/changeUser")
    @ApiOperation(value = "编辑用户信息", tags = "管理员用户相关功能")
    public ApiResponse changeUser(
            @RequestBody ChangeUserInformationRequest changeUserInformationRequest
    ) {
        iUserService.changeUser(changeUserInformationRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/adminResetPassword")
    @ApiOperation(value = "管理员重置密码", tags = "管理员用户相关功能")
    public ApiResponse adminResetPassword(
            @RequestBody AdminResetPasswordRequest adminResetPasswordRequest
    ) {
        iUserService.adminResetPassword(adminResetPasswordRequest);
        return ApiResponse.success();
    }

    @PostMapping(value = "/promoteUser")
    @ApiOperation(value = "给予用户特殊权限", tags = "管理员用户相关功能")
    public ApiResponse promoteUser(
            @RequestBody PromoteUserRequest promoteUserRequest
    ) {
        iUserService.promoteUser(promoteUserRequest);
        return ApiResponse.success();
    }

    /**
     * 页面
     */
    @RequestMapping("/index")
    public String index() {

        return "index";
    }

    /**
     * 导入excel
     */
    @RequestMapping("/importExcel")
    @ResponseBody
    public ApiResponse excelImport(@RequestParam(value = "filename") MultipartFile file, HttpSession session) {
        int result = 0;
        try {
            result = iUserService.excelUser(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result > 0) {
            return ApiResponse.success("excel文件数据导入成功！");
        } else {
            return ApiResponse.error("excel文件数据导入失败！");
        }

    }


}


