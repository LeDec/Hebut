package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.User;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
public interface IUserService extends IService<User> {
    /**
     * 注册
     *
     * @param registerRequest 注册请求
     * @return 注册信息
     * @author lidong
     */
    void register(RegisterRequest registerRequest);

    /**
     * 登录
     *
     * @param loginRequest 登录请求
     * @return 登录信息
     * @author lidong
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 查询个人信息
     *
     * @param commonRequest 登录请求
     * @return 个人信息
     * @author lidong
     */
    InformationResponse information(CommonRequest commonRequest);

    /**
     * 修改个人信息
     *
     * @param changeInformationRequest
     */
    void changeInformation(ChangeInformationRequest changeInformationRequest);

    /**
     * 修改密码
     *
     * @param changePasswordRequest
     */
    void changePassword(ChangePasswordRequest changePasswordRequest);

    /**
     * 修改用户类型
     *
     * @param changeUserTypeRequest
     */
    void changeUserType(ChangeUserTypeRequest changeUserTypeRequest);


    /**
     * 查看学生用户
     *
     * @param checkStudentRequest
     * @return 所有学生用户列表
     */
    Page<CheckStudentResponse> checkStudent(CheckStudentRequest checkStudentRequest);

    /**
     * 增加教师类用户
     *
     * @param addTeacherRequest
     */
    void addTeacher(AddTeacherRequest addTeacherRequest);

    /**
     * 删除教师用户
     *
     * @param deleteUserRequest
     */
    void deleteUser(DeleteUserRequest deleteUserRequest);

    /**
     * 编辑教师用户信息
     *
     * @param changeTeacherInformationRequest
     */
    void changeTeacher(ChangeTeacherInformationRequest changeTeacherInformationRequest);

    /**
     * 教师注册
     *
     * @param registerTeacherRequest
     */
    void registerTeacher(RegisterTeacherRequest registerTeacherRequest);

    /**
     * 审核教师注册
     *
     * @param auditTeacherRequest
     */
    void auditRegister(AuditTeacherRequest auditTeacherRequest);

    /**
     * 编辑用户信息
     *
     * @param changeUserInformationRequest
     */
    void changeUser(ChangeUserInformationRequest changeUserInformationRequest);

    /**
     * 管理员重置密码
     *
     * @param adminResetPasswordRequest
     */
    void adminResetPassword(AdminResetPasswordRequest adminResetPasswordRequest);

    /**
     * 修改教师用户类型
     *
     * @param changeUserTypeRequest
     */
    void changeTeacherType(ChangeUserTypeRequest changeUserTypeRequest);

    /**
     * 查看教师用户信息
     *
     * @param checkUserRequest
     * @return
     */
    Page<TeacherInformationResponse> checkTeacher(CheckUserRequest checkUserRequest);

    /**
     * 搜索学生用户信息
     *
     * @param searchStudentRequest
     * @return
     */
    PageUtil<CheckStudentResponse> searchStudent(SearchStudentRequest searchStudentRequest);

    /**
     * 搜索教师用户信息
     *
     * @param searchTeacherRequest
     * @return
     */
    PageUtil<TeacherInformationResponse> searchTeacher(SearchTeacherRequest searchTeacherRequest);

    /**
     * 查看匠心班学生信息
     *
     * @param checkUserRequest
     * @return
     */
    Page<CheckClassmatesResponse> checkClassmates(CheckUserRequest checkUserRequest);

    /**
     * 搜索匠心班学生信息
     *
     * @param searchClassmatesRequest
     * @return
     */
    PageUtil<CheckClassmatesResponse> searchClassmates(SearchClassmatesRequest searchClassmatesRequest);

    /**
     * 给予用户特殊权限
     *
     * @param promoteUserRequest
     */
    void promoteUser(PromoteUserRequest promoteUserRequest);

    /**
     * 查看用户项目经历
     *
     * @param checkUserExperienceRequest
     * @return
     */
    PageUtil<CheckUserExperienceResponse> checkUserExperience(CheckUserExperienceRequest checkUserExperienceRequest);

    /**
     * 查看待审核教师注册
     *
     * @param checkRequest
     * @return
     */
    Page<TeacherAuditResponse> checkRegisterTeacher(CheckRequest checkRequest);

    /**
     * 搜索待审核教师信息
     *
     * @param checkAuditTeacherRequest
     * @return
     */
    PageUtil<TeacherInformationResponse> checkAuditTeacher(CheckAuditTeacherRequest checkAuditTeacherRequest);

    /**
     * Excel导入用户
     *
     * @param file
     * @return
     */
    int excelUser(MultipartFile file) throws IOException;
}
