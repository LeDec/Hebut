package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.MD5Utils;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.*;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.mapper.*;
import com.Hebut.JiangXin.project.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    UserMapper userMapper;
    @Resource
    InstituteMapper instituteMapper;
    @Resource
    InstituteMajorMapper InstituteMajorMapper;
    @Resource
    MajorMapper majorMapper;
    @Resource
    ProjectUserMapper projectUserMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    ApplicationMapper applicationMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    BatchMapper batchMapper;
    @Resource
    BatchUserMapper batchUserMapper;

    private long pages(Page page) {
        long pages = page.getTotal() / page.getSize();
        if (page.getSize() == 0) {
            pages = 0L;
        }
        if (page.getTotal() % page.getSize() != 0) {
            pages++;
        }
        return pages;
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getPasswordRepeat())) {
            throw new CustomException(ErrorEnum.PASSWORD_REPEAT.getCode(), "两次密码输入不一致");
        }
        if ("".equals(registerRequest.getUserId()) || "".equals(registerRequest.getPassword())
                || "".equals(registerRequest.getInstituteId()) || "".equals(registerRequest.getName())
                || "".equals(registerRequest.getMajorId()) || "".equals(registerRequest.getEMail())) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "必要信息未填写");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, registerRequest.getUserId())
        );
        if (user != null) {
            throw new CustomException(ErrorEnum.ID_EQUAL.getCode(), "该学号已被成功注册。");
        }
        User newUser = new User();
        newUser.setUserId(registerRequest.getUserId());
        newUser.setUserPasswords(MD5Utils.inputPassToFormPass(registerRequest.getPassword()));
        newUser.setUserName(registerRequest.getName());
        newUser.setType(UserTypeEnum.STUDENT.getCode());
        newUser.setInstituteId(registerRequest.getInstituteId());
        newUser.setMajorId(registerRequest.getMajorId());
        newUser.setEMail(registerRequest.getEMail());
        String token = "token-" + UUID.randomUUID().toString().replace("\\-", "");
        newUser.setToken(token);
        userMapper.insert(newUser);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String password = MD5Utils.inputPassToFormPass(loginRequest.getPassword());
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, loginRequest.getUserId())
                        .eq(User::getUserPasswords, password)
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.MISMATCH_ERROR.getCode(), "密码或账号错误，请确认后在输入。");
        }
        /**
         * try {
         kaptcha.validate(loginRequest.getCode());
         } catch (KaptchaIncorrectException e) {
         throw new CustomException("验证码不正确");
         } catch (KaptchaNotFoundException e) {
         throw new CustomException("验证码未找到");
         } catch (KaptchaRenderException e) {
         throw new CustomException("验证码渲染失败");
         } catch (KaptchaTimeoutException e) {
         throw new CustomException("验证码过期");
         }
         **/
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getUserId());
        loginResponse.setUserName(user.getUserName());
        loginResponse.setType(user.getType());
        String token = "token-" + UUID.randomUUID().toString().replace("\\-", "");
        user.setToken(token);
        userMapper.updateById(user);
        loginResponse.setToken(token);
        return loginResponse;
    }

    @Override
    public InformationResponse information(CommonRequest commonRequest) {
        InformationResponse informationResponse = new InformationResponse();
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, commonRequest.getUserId())
                        .eq(User::getToken, commonRequest.getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在，或账号在异地登录");
        }
        Institute institute = instituteMapper.selectOne(
                Wrappers.lambdaQuery(Institute.class)
                        .eq(Institute::getInstituteId, user.getInstituteId())
                        .eq(Institute::getInstituteIsDel, IsDelEnum.UNDELETE.getCode())
        );
        if (institute == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "学院不存在");
        }
        List<InstituteMajor> InstituteMajors = InstituteMajorMapper.selectList(
                Wrappers.lambdaQuery(InstituteMajor.class)
                        .eq(InstituteMajor::getInstituteId, institute.getInstituteId())
                        .eq(InstituteMajor::getImrelationIsDel, IsDelEnum.UNDELETE.getCode())
        );
        if (InstituteMajors == null) {
            throw new CustomException(ErrorEnum.MISMATCH_ERROR.getCode(), "学院专业信息不匹配，请联系管理员");
        }
        Major major = majorMapper.selectOne(
                Wrappers.lambdaQuery(Major.class)
                        .eq(Major::getMajorId, user.getMajorId())
                        .eq(Major::getMajorIsDel, IsDelEnum.UNDELETE.getCode())
        );
        if (major == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "专业不存在");
        }
        informationResponse.setUserId(user.getUserId());
        informationResponse.setUserName(user.getUserName());
        informationResponse.setEmail(user.getEMail());
        informationResponse.setInstitute(institute.getInstituteName());
        informationResponse.setMajor(major.getMajorName());
        informationResponse.setType(user.getType());
        informationResponse.setIsPromote(user.getIsPromote());
        return informationResponse;
    }

    @Override
    public void changeInformation(ChangeInformationRequest changeInformationRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeInformationRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, changeInformationRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在，或账号在异地登录");
        }
        user.setInstituteId(changeInformationRequest.getNewInstitute());
        user.setMajorId(changeInformationRequest.getNewMajor());
        user.setEMail(changeInformationRequest.getNewEMail());
        userMapper.updateById(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changePasswordRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, changePasswordRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在，或账号在异地登录");
        }
        user.setUserPasswords(MD5Utils.inputPassToFormPass(changePasswordRequest.getNewPassword()));
        userMapper.updateById(user);
    }

    @Override
    public void changeUserType(ChangeUserTypeRequest changeUserTypeRequest) {
        if (!changeUserTypeRequest.getUserType().equals(UserTypeEnum.ADMIN.getCode())
                && !changeUserTypeRequest.getUserType().equals(UserTypeEnum.CLASS_TEACHER.getCode())
                && !changeUserTypeRequest.getUserType().equals(UserTypeEnum.TEACHER.getCode())
                && !changeUserTypeRequest.getUserType().equals(UserTypeEnum.STUDENT.getCode())
                && !changeUserTypeRequest.getUserType().equals(UserTypeEnum.ENTERPRISE.getCode())
        ) {
            throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "对应类型不存在");
        }
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeUserTypeRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, changeUserTypeRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.SYSTEM_ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "没有高级管理员权限");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeUserTypeRequest.getUserId())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "目标用户不存在");
        }
        user.setType(changeUserTypeRequest.getUserType());
        userMapper.updateById(user);

    }

    @Override
    public Page<CheckStudentResponse> checkStudent(CheckStudentRequest checkStudentRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkStudentRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkStudentRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Page page = new Page(checkStudentRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkStudentRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<User> userPage = userMapper.selectPage(
                page,
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getType, UserTypeEnum.STUDENT.getCode())
        );
        List<CheckStudentResponse> checkStudentResponseList = new ArrayList<>();
        for (User information : userPage.getRecords()) {
            CheckStudentResponse checkStudentResponse = new CheckStudentResponse();
            checkStudentResponse.setUserId(information.getUserId());
            checkStudentResponse.setUserName(information.getUserName());
            checkStudentResponse.setType(information.getType());
            checkStudentResponse.setEmail(information.getEMail());
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, information.getInstituteId())
            );
            checkStudentResponse.setInstitute(institute.getInstituteName());
            Major major = majorMapper.selectOne(
                    Wrappers.lambdaQuery(Major.class)
                            .eq(Major::getMajorId, information.getMajorId())
            );
            checkStudentResponse.setMajor(major.getMajorName());
            List<ProjectUser> projectUser = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getUserId, information.getUserId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            if (projectUser.size() == 0) {
                LocalDateTime min = LocalDateTime.of(2007, 12, 03, 10, 15, 30);
                checkStudentResponse.setBatchName(null);
                checkStudentResponse.setEndTime(min);
            } else {
                BatchProject batchProject = batchProjectMapper.selectOne(
                        Wrappers.lambdaQuery(BatchProject.class)
                                .eq(BatchProject::getProjectId, projectUser.get(0).getProjectId())
                );
                Batch batch = batchMapper.selectOne(
                        Wrappers.lambdaQuery(Batch.class)
                                .eq(Batch::getBatchId, batchProject.getBatchId())
                );
                checkStudentResponse.setBatchName(batch.getBatchName());
                checkStudentResponse.setEndTime(batch.getDefendDeadline());
            }
            checkStudentResponseList.add(checkStudentResponse);
        }
        checkStudentResponseList.sort(CheckStudentResponse.BatchName);
        Page<CheckStudentResponse> checkStudentResponsePages = new Page<>(checkStudentRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkStudentRequest.getCheckRequest().getPageRequest().getPageSize());
        BeanUtils.copyProperties(userPage,checkStudentResponsePages);
        checkStudentResponsePages.setRecords(checkStudentResponseList);
        return checkStudentResponsePages;
    }

    @Override
    public void addTeacher(AddTeacherRequest addTeacherRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addTeacherRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, addTeacherRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addTeacherRequest.getUserId())
        );
        if (user != null) {
            throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "该学工号已注册");
        }
        User teacher = new User();
        String token = "token-" + UUID.randomUUID().toString().replace("\\-", "");
        teacher.setToken(token);
        teacher.setUserId(addTeacherRequest.getUserId());
        teacher.setUserPasswords(MD5Utils.inputPassToFormPass("hebut" + addTeacherRequest.getUserId()));
        teacher.setMajorId(MajorEnum.TEACHER.getCode());
        teacher.setInstituteId(addTeacherRequest.getInstituteId());
        teacher.setEMail(addTeacherRequest.getEMail());
        teacher.setUserName(addTeacherRequest.getName());
        teacher.setType(addTeacherRequest.getTypeId());
        userMapper.insert(teacher);
    }

    @Override
    public void deleteUser(DeleteUserRequest deleteUserRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, deleteUserRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, deleteUserRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, deleteUserRequest.getUserId())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到该用户");
        }
        userMapper.deleteById(user);
    }

    @Override
    public void changeTeacher(ChangeTeacherInformationRequest changeTeacherInformationRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeTeacherInformationRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, changeTeacherInformationRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        User teacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeTeacherInformationRequest.getUserId())
        );
        teacher.setUserName(changeTeacherInformationRequest.getName());
        teacher.setEMail(changeTeacherInformationRequest.getEMail());
        teacher.setInstituteId(changeTeacherInformationRequest.getInstituteId());
        userMapper.updateById(teacher);
    }

    @Override
    public void registerTeacher(RegisterTeacherRequest registerTeacherRequest) {
        if (!registerTeacherRequest.getPassword().equals(registerTeacherRequest.getPasswordRepeat())) {
            throw new CustomException(ErrorEnum.PASSWORD_REPEAT.getCode(), "两次密码输入不一致");
        }
        if ("".equals(registerTeacherRequest.getUserId()) || "".equals(registerTeacherRequest.getPassword())
                || "".equals(registerTeacherRequest.getInstituteId()) || "".equals(registerTeacherRequest.getName())
                || "".equals(registerTeacherRequest.getEMail())) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "必要信息未填写");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, registerTeacherRequest.getUserId())
        );
        if (user != null) {
            throw new CustomException(ErrorEnum.ID_EQUAL.getCode(), "该学号已被成功注册。");
        }
        User newUser = new User();
        newUser.setUserId(registerTeacherRequest.getUserId());
        newUser.setUserPasswords(MD5Utils.inputPassToFormPass(registerTeacherRequest.getPassword()));
        newUser.setUserName(registerTeacherRequest.getName());
        newUser.setType(registerTeacherRequest.getUserType());
        newUser.setInstituteId(registerTeacherRequest.getInstituteId());
        newUser.setMajorId(MajorEnum.TEACHER.getCode());
        newUser.setEMail(registerTeacherRequest.getEMail());
        String token = "token-" + UUID.randomUUID().toString().replace("\\-", "");
        newUser.setToken(token);
        userMapper.insert(newUser);
    }

    @Override
    public void auditRegister(AuditTeacherRequest auditTeacherRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, auditTeacherRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, auditTeacherRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        User teacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, auditTeacherRequest.getUseId())
                        .between(User::getType, UserTypeEnum.CLASS_TEACHER.getCode(), UserTypeEnum.EXPERT.getCode())
        );
        if (teacher == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到该待审核教师用户");
        }
        if ("1".equals(auditTeacherRequest.getIsPass())) {
            userMapper.updateById(teacher);
        } else if ("0".equals(auditTeacherRequest.getIsPass())) {
            userMapper.deleteById(teacher);
        } else {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "状态码出错");
        }
    }

    @Override
    public void changeUser(ChangeUserInformationRequest changeUserInformationRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeUserInformationRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, changeUserInformationRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeUserInformationRequest.getUserId())
        );
        user.setUserName(changeUserInformationRequest.getName());
        user.setEMail(changeUserInformationRequest.getEMail());
        user.setType(changeUserInformationRequest.getUserType());
        user.setMajorId(changeUserInformationRequest.getMajorId());
        user.setInstituteId(changeUserInformationRequest.getInstituteId());
        userMapper.updateById(user);
    }

    @Override
    public void adminResetPassword(AdminResetPasswordRequest adminResetPasswordRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, adminResetPasswordRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, adminResetPasswordRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, adminResetPasswordRequest.getUserId())
        );
        user.setUserPasswords(MD5Utils.inputPassToFormPass("hebut" + user.getUserId()));
        userMapper.updateById(user);

    }

    @Override
    public void changeTeacherType(ChangeUserTypeRequest changeUserTypeRequest) {
        if (!changeUserTypeRequest.getUserType().equals(UserTypeEnum.CLASS_TEACHER.getCode())
                && !changeUserTypeRequest.getUserType().equals(UserTypeEnum.TEACHER.getCode())
                && !changeUserTypeRequest.getUserType().equals(UserTypeEnum.STUDENT.getCode())
                && !changeUserTypeRequest.getUserType().equals(UserTypeEnum.EXPERT.getCode())
                && !changeUserTypeRequest.getUserType().equals(UserTypeEnum.ENTERPRISE.getCode())
        ) {
            throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "对应类型不存在或没有权限");
        }
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeUserTypeRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, changeUserTypeRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "没有管理员权限");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeUserTypeRequest.getUserId())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "目标用户不存在");
        }
        user.setType(changeUserTypeRequest.getUserType());
        userMapper.updateById(user);
    }

    @Override
    public Page<TeacherInformationResponse> checkTeacher(CheckUserRequest checkUserRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkUserRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkUserRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Page<User> page = new Page<>();
        page.setCurrent(checkUserRequest.getCheckRequest().getPageRequest().getCurrentPage());
        page.setSize(checkUserRequest.getCheckRequest().getPageRequest().getPageSize());
        page.addOrder(OrderItem.ascs("type", "institute_Id", "user_Id"));
        Page<TeacherInformationResponse> teacherInformationResponsePage = new Page<>();
        Page<User> userPage = userMapper.selectPage(
                page,
                Wrappers.lambdaQuery(User.class)
                        .ge(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
                        .le(User::getType, UserTypeEnum.EXPERT.getCode())
        );
        List<TeacherInformationResponse> teacherInformationResponseList = new ArrayList<>();
        for (User information : userPage.getRecords()) {
            TeacherInformationResponse teacherInformationResponse = new TeacherInformationResponse();
            teacherInformationResponse.setUserId(information.getUserId());
            teacherInformationResponse.setUserName(information.getUserName());
            teacherInformationResponse.setType(information.getType());
            teacherInformationResponse.setEmail(information.getEMail());
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, information.getInstituteId())
            );
            teacherInformationResponse.setInstitute(institute.getInstituteName());
            teacherInformationResponse.setIsPromote(information.getIsPromote());
            teacherInformationResponse.setIsAudit(information.getIsAudit());
            teacherInformationResponseList.add(teacherInformationResponse);
        }
        teacherInformationResponsePage.setTotal(teacherInformationResponseList.size());
        teacherInformationResponsePage.setCurrent(checkUserRequest.getCheckRequest().getPageRequest().getCurrentPage());
        teacherInformationResponsePage.setSize(checkUserRequest.getCheckRequest().getPageRequest().getPageSize());
        teacherInformationResponsePage.setRecords(teacherInformationResponseList);
        long pages = userPage.getTotal() / userPage.getSize();
        if (userPage.getSize() == 0) {
            pages = 0L;
        }
        if (userPage.getTotal() % userPage.getSize() != 0) {
            pages++;
        }
        return teacherInformationResponsePage;
    }

    @Override
    public PageUtil<CheckStudentResponse> searchStudent(SearchStudentRequest searchStudentRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchStudentRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, searchStudentRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<CheckStudentResponse> checkStudentResponseList = new ArrayList<>();
        List<User> userList = userMapper.selectList(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getType, UserTypeEnum.STUDENT.getCode())
                        .like(User::getUserName, searchStudentRequest.getStudentName())
                        .like(User::getUserId, searchStudentRequest.getStudentId())
        );
        for (User student : userList) {
            CheckStudentResponse checkStudentResponse = new CheckStudentResponse();
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, student.getInstituteId())
                            .like(Institute::getInstituteName, searchStudentRequest.getInstitute())
            );
            if (institute == null) {
                continue;
            }
            Major major = majorMapper.selectOne(
                    Wrappers.lambdaQuery(Major.class)
                            .eq(Major::getMajorId, student.getMajorId())
                            .like(Major::getMajorName, searchStudentRequest.getMajor())
            );
            if (major == null) {
                continue;
            }
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getUserId, student.getUserId())
            );
            if (ProjectUserList.size() == 0 && Objects.equals(searchStudentRequest.getProjectName(), "") && Objects.equals(searchStudentRequest.getBatchName(), "")) {
                checkStudentResponse.setUserId(student.getUserId());
                checkStudentResponse.setUserName(student.getUserName());
                checkStudentResponse.setType(student.getType());
                checkStudentResponse.setEmail(student.getEMail());
                checkStudentResponse.setInstitute(institute.getInstituteName());
                checkStudentResponse.setMajor(major.getMajorName());
                checkStudentResponseList.add(checkStudentResponse);
            }
            for (ProjectUser ProjectUser : ProjectUserList) {
                Project project = projectMapper.selectOne(
                        Wrappers.lambdaQuery(Project.class)
                                .eq(Project::getProjectId, ProjectUser.getProjectId())
                                .like(Project::getProjectName, searchStudentRequest.getProjectName())
                );
                if (project == null) {
                    continue;
                }
                BatchProject batchProject = batchProjectMapper.selectOne(
                        Wrappers.lambdaQuery(BatchProject.class)
                                .eq(BatchProject::getProjectId, project.getProjectId())
                );
                Batch batch = batchMapper.selectOne(
                        Wrappers.lambdaQuery(Batch.class)
                                .eq(Batch::getBatchId, batchProject.getBatchId())
                                .like(Batch::getBatchName, searchStudentRequest.getBatchName())
                );
                if (batch == null) {
                    continue;
                }
                checkStudentResponse.setBatchName(batch.getBatchName());
                checkStudentResponse.setUserId(student.getUserId());
                checkStudentResponse.setUserName(student.getUserName());
                checkStudentResponse.setType(student.getType());
                checkStudentResponse.setEmail(student.getEMail());
                checkStudentResponse.setInstitute(institute.getInstituteName());
                checkStudentResponse.setMajor(major.getMajorName());
                checkStudentResponseList.add(checkStudentResponse);
                break;
            }
        }
        checkStudentResponseList.sort(CheckStudentResponse.studentId);
        PageUtil<CheckStudentResponse> checkStudentResponsePage = new PageUtil<>(searchStudentRequest.getCheckRequest().getPageRequest().getCurrentPage(), searchStudentRequest.getCheckRequest().getPageRequest().getPageSize(), checkStudentResponseList);
        return checkStudentResponsePage;
    }

    @Override
    public PageUtil<TeacherInformationResponse> searchTeacher(SearchTeacherRequest searchTeacherRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchTeacherRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, searchTeacherRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<User> userList = new ArrayList<>();
        if (searchTeacherRequest.getUserType() != null) {
            userList = userMapper.selectList(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getType, searchTeacherRequest.getUserType())
                            .like(User::getUserId, searchTeacherRequest.getTeacherId())
                            .like(User::getUserName, searchTeacherRequest.getTeacherName())
            );
        } else {
            userList = userMapper.selectList(
                    Wrappers.lambdaQuery(User.class)
                            .between(User::getType, UserTypeEnum.CLASS_TEACHER.getCode(), UserTypeEnum.EXPERT.getCode())
                            .like(User::getUserId, searchTeacherRequest.getTeacherId())
                            .like(User::getUserName, searchTeacherRequest.getTeacherName())
            );
        }
        List<TeacherInformationResponse> informationResponseList = new ArrayList<>();
        for (User information : userList) {
            TeacherInformationResponse teacherInformationResponse = new TeacherInformationResponse();
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, information.getInstituteId())
                            .like(Institute::getInstituteName, searchTeacherRequest.getInstitute())
            );
            if (institute == null) {
                continue;
            }
            teacherInformationResponse.setInstitute(institute.getInstituteName());
            teacherInformationResponse.setUserId(information.getUserId());
            teacherInformationResponse.setUserName(information.getUserName());
            teacherInformationResponse.setType(information.getType());
            teacherInformationResponse.setEmail(information.getEMail());
            teacherInformationResponse.setIsPromote(information.getIsPromote());
            informationResponseList.add(teacherInformationResponse);
        }
        PageUtil<TeacherInformationResponse> informationResponsePage = new PageUtil<>(searchTeacherRequest.getCheckRequest().getPageRequest().getCurrentPage(), searchTeacherRequest.getCheckRequest().getPageRequest().getPageSize(), informationResponseList);
        return informationResponsePage;
    }

    @Override
    public Page<CheckClassmatesResponse> checkClassmates(CheckUserRequest checkUserRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkUserRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkUserRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Page<CheckClassmatesResponse> checkClassmatesResponsePage = new Page<>(checkUserRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkUserRequest.getCheckRequest().getPageRequest().getPageSize());
        Page page = new Page(checkUserRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkUserRequest.getCheckRequest().getPageRequest().getPageSize());
        page.addOrder(OrderItem.asc("user_id"));
        Page<BatchUser> batchUserPage = batchUserMapper.selectPage(
                page,
                Wrappers.lambdaQuery(BatchUser.class)
                        .eq(BatchUser::getBatchId, checkUserRequest.getBatchId())
        );
        BeanUtils.copyProperties(batchUserPage,checkClassmatesResponsePage);
        List<CheckClassmatesResponse> checkClassmatesResponses = new ArrayList<>();
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, checkUserRequest.getBatchId())
        );
        for (BatchUser batchUser : batchUserPage.getRecords()) {
            User information = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, batchUser.getUserId())
            );
            ProjectUser projectUser = projectUserMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getBatchId,batch.getBatchId())
                            .eq(ProjectUser::getUserId,batchUser.getUserId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, projectUser.getProjectId())
            );
            CheckClassmatesResponse checkClassmatesResponse = new CheckClassmatesResponse();
            checkClassmatesResponse.setUserId(information.getUserId());
            checkClassmatesResponse.setUserName(information.getUserName());
            checkClassmatesResponse.setType(information.getType());
            checkClassmatesResponse.setEmail(information.getEMail());
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, information.getInstituteId())
            );
            checkClassmatesResponse.setInstitute(institute.getInstituteName());
            Major major = majorMapper.selectOne(
                    Wrappers.lambdaQuery(Major.class)
                            .eq(Major::getMajorId, information.getMajorId())
            );
            checkClassmatesResponse.setMajor(major.getMajorName());
            Application application = applicationMapper.selectOne(
                    Wrappers.lambdaQuery(Application.class)
                            .eq(Application::getUserId, information.getUserId())
                            .eq(Application::getProjectId, project.getProjectId())
            );
            if (application == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), information.getUserName() + "  与  " + project.getProjectName() + "没有报名关系");
            }
            checkClassmatesResponse.setEnrollId(application.getApplicationId());
            checkClassmatesResponse.setProjectId(project.getProjectId());
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            checkClassmatesResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkClassmatesResponse.setProjectName(project.getProjectName().substring(index + 1));
            checkClassmatesResponses.add(checkClassmatesResponse);
        }
        checkClassmatesResponsePage.setRecords(checkClassmatesResponses);
        return checkClassmatesResponsePage;
    }

    @Override
    public PageUtil<CheckClassmatesResponse> searchClassmates(SearchClassmatesRequest searchClassmatesRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchClassmatesRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, searchClassmatesRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, searchClassmatesRequest.getBatchId())
        );
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, batch.getBatchId())
        );
        List<CheckClassmatesResponse> checkClassmatesResponseList = new ArrayList<>();
        for (BatchProject batchProject : batchProjectList) {
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, batchProject.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            for (ProjectUser ProjectUser : ProjectUserList) {
                User information = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, ProjectUser.getUserId())
                                .like(User::getUserId, searchClassmatesRequest.getStudentId())
                                .like(User::getUserName, searchClassmatesRequest.getStudentName())
                                .eq(User::getType, UserTypeEnum.STUDENT.getCode())
                );
                if (information == null) {
                    continue;
                }
                Project project = projectMapper.selectOne(
                        Wrappers.lambdaQuery(Project.class)
                                .eq(Project::getProjectId, ProjectUser.getProjectId())
                                .eq(Project::getProjectIsLocal, IsLocalEnum.LOCAL.getCode())
                );
                if (project == null) {
                    continue;
                }
                int index = project.getProjectName().lastIndexOf('_');
                if (index == -1) {
                    throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
                }
                if (!Objects.equals(searchClassmatesRequest.getProjectSymbol(), "")) {
                    if (!project.getProjectName().substring(0, index).contains(searchClassmatesRequest.getProjectSymbol())) {
                        continue;
                    }
                } else if (!Objects.equals(searchClassmatesRequest.getProjectName(), "")) {
                    if (!project.getProjectName().substring(index + 1).contains(searchClassmatesRequest.getProjectName())) {
                        continue;
                    }
                }
                CheckClassmatesResponse checkClassmatesResponse = new CheckClassmatesResponse();
                checkClassmatesResponse.setUserId(information.getUserId());
                checkClassmatesResponse.setUserName(information.getUserName());
                checkClassmatesResponse.setType(information.getType());
                checkClassmatesResponse.setEmail(information.getEMail());
                Institute institute = instituteMapper.selectOne(
                        Wrappers.lambdaQuery(Institute.class)
                                .eq(Institute::getInstituteId, information.getInstituteId())
                                .like(Institute::getInstituteName, searchClassmatesRequest.getInstitute())
                );
                checkClassmatesResponse.setInstitute(institute.getInstituteName());
                Major major = majorMapper.selectOne(
                        Wrappers.lambdaQuery(Major.class)
                                .eq(Major::getMajorId, information.getMajorId())
                                .like(Major::getMajorName, searchClassmatesRequest.getMajor())
                );
                checkClassmatesResponse.setMajor(major.getMajorName());
                Application application = applicationMapper.selectOne(
                        Wrappers.lambdaQuery(Application.class)
                                .eq(Application::getUserId, information.getUserId())
                                .eq(Application::getProjectId, project.getProjectId())
                );
                if (application == null) {
                    throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), information.getUserName() + "  与  " + project.getProjectName() + "没有报名关系");
                }
                checkClassmatesResponse.setEnrollId(application.getApplicationId());
                checkClassmatesResponse.setProjectId(project.getProjectId());
                checkClassmatesResponse.setProjectSymbol(project.getProjectName().substring(0, index));
                checkClassmatesResponse.setProjectName(project.getProjectName().substring(index + 1));
                checkClassmatesResponseList.add(checkClassmatesResponse);
            }
        }
        checkClassmatesResponseList.sort(CheckClassmatesResponse.studentId);
        PageUtil<CheckClassmatesResponse> checkClassmatesResponsePage = new PageUtil<>(searchClassmatesRequest.getCheckRequest().getPageRequest().getCurrentPage(), searchClassmatesRequest.getCheckRequest().getPageRequest().getPageSize(), checkClassmatesResponseList);
        return checkClassmatesResponsePage;
    }

    @Override
    public void promoteUser(PromoteUserRequest promoteUserRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, promoteUserRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, promoteUserRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        for (String userId : promoteUserRequest.getUserIdList()) {
            User user = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, userId)
            );
            user.setIsPromote(IsPromoteEnum.PROMOTE.getCode());
            userMapper.updateById(user);
        }
    }

    @Override
    public PageUtil<CheckUserExperienceResponse> checkUserExperience(CheckUserExperienceRequest checkUserExperienceRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkUserExperienceRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkUserExperienceRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<CheckUserExperienceResponse> checkUserExperienceResponseList = new ArrayList<>();
        List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getUserId, checkUserExperienceRequest.getUserId())
        );
        for (ProjectUser ProjectUser : ProjectUserList) {
            CheckUserExperienceResponse checkUserExperienceResponse = new CheckUserExperienceResponse();
            BatchProject batchProject = batchProjectMapper.selectOne(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getProjectId, ProjectUser.getProjectId())
            );
            Batch batch = batchMapper.selectOne(
                    Wrappers.lambdaQuery(Batch.class)
                            .eq(Batch::getBatchId, batchProject.getBatchId())
            );
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, batchProject.getProjectId())
            );
            checkUserExperienceResponse.setBatchId(batch.getBatchId());
            checkUserExperienceResponse.setBatchName(batch.getBatchName());
            checkUserExperienceResponse.setStartTime(batch.getEnrollBeginning());
            checkUserExperienceResponse.setProjectId(project.getProjectId());
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            checkUserExperienceResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkUserExperienceResponse.setProjectName(project.getProjectName().substring(index + 1));
            String relation = null;
            if (Objects.equals(ProjectUser.getType(), ProjectUserRelationEnum.APPLICANT.getCode())) {
                relation = ProjectUserRelationEnum.APPLICANT.getMsg();
            } else if (Objects.equals(ProjectUser.getType(), ProjectUserRelationEnum.EXPERT.getCode())) {
                relation = ProjectUserRelationEnum.EXPERT.getMsg();
            } else if (Objects.equals(ProjectUser.getType(), ProjectUserRelationEnum.TEACHER.getCode())) {
                relation = ProjectUserRelationEnum.TEACHER.getMsg();
            } else if (Objects.equals(ProjectUser.getType(), ProjectUserRelationEnum.PARTNER.getCode())) {
                relation = ProjectUserRelationEnum.PARTNER.getMsg();
            }
            checkUserExperienceResponse.setRelationId(relation);
            checkUserExperienceResponseList.add(checkUserExperienceResponse);
        }
        checkUserExperienceResponseList.sort(CheckUserExperienceResponse.batchTime);
        PageUtil<CheckUserExperienceResponse> checkUserExperienceResponsePage = new PageUtil<>(checkUserExperienceRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkUserExperienceRequest.getCheckRequest().getPageRequest().getPageSize(), checkUserExperienceResponseList);
        return checkUserExperienceResponsePage;
    }

    @Override
    public Page<TeacherAuditResponse> checkRegisterTeacher(CheckRequest checkRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Page<TeacherAuditResponse> teacherAuditResponsePage = new Page<>(checkRequest.getPageRequest().getCurrentPage(), checkRequest.getPageRequest().getPageSize());
        Page page = new Page(checkRequest.getPageRequest().getCurrentPage(), checkRequest.getPageRequest().getPageSize());
        page.addOrder(OrderItem.asc("user_id"));
        Page<User> userPage = userMapper.selectPage(
                page,
                Wrappers.lambdaQuery(User.class)
                        .ge(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
                        .le(User::getType, UserTypeEnum.EXPERT.getCode())
                        .eq(User::getIsAudit, IsDelEnum.DELETE.getCode())
        );
        BeanUtils.copyProperties(userPage,teacherAuditResponsePage);
        List<TeacherAuditResponse> teacherAuditResponses = new ArrayList<>();
        for (User information : userPage.getRecords()) {
            TeacherAuditResponse teacherAuditResponse = new TeacherAuditResponse();
            teacherAuditResponse.setUserId(information.getUserId());
            teacherAuditResponse.setUserName(information.getUserName());
            teacherAuditResponse.setType(information.getType());
            teacherAuditResponse.setEmail(information.getEMail());
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, information.getInstituteId())
            );
            teacherAuditResponse.setInstitute(institute.getInstituteName());
            teacherAuditResponses.add(teacherAuditResponse);
        }
        teacherAuditResponsePage.setRecords(teacherAuditResponses);
        return teacherAuditResponsePage;
    }

    @Override
    public PageUtil<TeacherInformationResponse> checkAuditTeacher(CheckAuditTeacherRequest checkAuditTeacherRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkAuditTeacherRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkAuditTeacherRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }

        List<User> userList = userMapper.selectList(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getType, UserTypeEnum.TEACHER.getCode())
        );
        List<TeacherInformationResponse> informationResponseList = new ArrayList<>();
        for (User information : userList) {
            TeacherInformationResponse teacherInformationResponse = new TeacherInformationResponse();
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, information.getInstituteId())
            );
            if (institute == null) {
                continue;
            }
            teacherInformationResponse.setInstitute(institute.getInstituteName());
            teacherInformationResponse.setUserId(information.getUserId());
            teacherInformationResponse.setUserName(information.getUserName());
            teacherInformationResponse.setType(information.getType());
            teacherInformationResponse.setEmail(information.getEMail());
            teacherInformationResponse.setIsPromote(information.getIsPromote());
            informationResponseList.add(teacherInformationResponse);
        }
        PageUtil<TeacherInformationResponse> informationResponsePage = new PageUtil<>(checkAuditTeacherRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkAuditTeacherRequest.getCheckRequest().getPageRequest().getPageSize(), informationResponseList);
        return informationResponsePage;
    }

    @Override
    public int excelUser(MultipartFile file) throws IOException {


        int result = 0;
        List<User> userList = new ArrayList<>();
        //判断文件版本

        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);

        InputStream ins = file.getInputStream();

        Workbook wb = null;

        if (suffix.equals("xlsx")) {

            wb = new XSSFWorkbook(ins);

        } else {
            wb = new HSSFWorkbook(ins);
        }
        //获取excel表单

        Sheet sheet = wb.getSheetAt(0);

        if (null != sheet) {
            for (int line = 1; line <= sheet.getLastRowNum(); line++) {
                User user = new User();
                Row row = sheet.getRow(line);
                if (null == row) {
                    continue;
                }
                //判断单元格类型是否为文本类型
                if (1 != row.getCell(0).getCellType()) {
                    throw new CustomException("单元格类型不是文本类型！");
                }

                //获取第一个单元格的内容

                String userId = row.getCell(0).getStringCellValue();
                row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                 //获取第二个单元格的内容
                String userName = row.getCell(1).getStringCellValue();

                row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                String institute = row.getCell(2).getStringCellValue();

                row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                String major = row.getCell(3).getStringCellValue();

                user.setUserId(userId);
                user.setUserName(userName);
                user.setEMail("@");
                Institute userInstitute;
                userInstitute = instituteMapper.selectOne(
                        Wrappers.lambdaQuery(Institute.class)
                                .eq(Institute::getInstituteName, institute)
                );
                if (userInstitute == null) {
                    throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "导入文件数据出错");
                }
                Major userMajor = majorMapper.selectOne(
                        Wrappers.lambdaQuery(Major.class)
                                .eq(Major::getMajorName, major)
                );
                if (major == null) {
                    throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "导入文件数据出错");
                }
                user.setInstituteId(userInstitute.getInstituteId());
                user.setMajorId(userMajor.getMajorId());
                user.setUserPasswords(MD5Utils.inputPassToFormPass("hebut" + user.getUserId()));
                user.setType(UserTypeEnum.STUDENT.getCode());
                String token = "token-" + UUID.randomUUID().toString().replace("\\-", "");
                user.setToken(token);
                userList.add(user);
            }

            for (User userInfo : userList) {

                //判断数据库表中是否存在用户记录，若存在，则更新，不存在，则保存记录
                String name = userInfo.getUserName();

                int count = userMapper.selectCount(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserName, name)
                );
                if (0 == count) {
                    result = userMapper.insert(userInfo);
                } else {
                    result = userMapper.updateById(userInfo);
                }
            }
        }

        return result;
    }


}


