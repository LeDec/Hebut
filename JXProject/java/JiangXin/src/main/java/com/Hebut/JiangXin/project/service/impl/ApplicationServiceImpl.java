package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.*;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.mapper.*;
import com.Hebut.JiangXin.project.service.IApplicationService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 报名表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-06
 */
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements IApplicationService {

    @Resource
    ApplicationMapper applicationMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    ProjectUserMapper ProjectUserMapper;
    @Resource
    InformMapper informMapper;
    @Resource
    InstituteMapper instituteMapper;
    @Resource
    BatchMapper batchMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    ProjectMajorMapper ProjectMajorMapper;
    @Resource
    MajorMapper majorMapper;
    @Resource
    InstituteMajorMapper InstituteMajorMapper;

    @Override
    public void enroll(EnrollRequest enrollRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, enrollRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, enrollRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您还未登录或账号在异地登录");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, enrollRequest.getProjectId())
                        .gt(Project::getProjectNeedNumber, 0)
        );
        BatchProject batchProject = batchProjectMapper.selectOne(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getProjectId, project.getProjectId())
        );
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, batchProject.getBatchId())
        );
        if (project == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "项目不存在或者项目所需人数为0");
        }
        if (LocalDateTime.now().isAfter(batch.getEnrollDeadline())) {
            throw new CustomException(ErrorEnum.OVERDUE_ERROR.getCode(), "已过报名时间");
        }
        List<Application> applicationList = applicationMapper.selectList(
                Wrappers.lambdaQuery(Application.class)
                        .eq(Application::getProjectId, enrollRequest.getProjectId())
                        .eq(Application::getUserId, enrollRequest.getCommonRequest().getUserId())
        );
        int applicationCount = applicationMapper.selectCount(
                Wrappers.lambdaQuery(Application.class)
                        .eq(Application::getProjectId, enrollRequest.getProjectId())
                        .eq(Application::getUserId, enrollRequest.getCommonRequest().getUserId())
        );
        if (applicationCount > 0) {
            throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "请勿重复报名");
        }
        for (Application application : applicationList) {
            Project project1 = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, application.getProjectId())
                            .eq(Project::getProjectIsLocal, IsLocalEnum.LOCAL.getCode())
            );
            if (project1 != null) {
                throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "只可以报名一个匠心项目");
            }
        }
        Application application = new Application();
        String applicationId = enrollRequest.getCommonRequest().getUserId() + "-" + enrollRequest.getProjectId() + "-" + UUID.randomUUID().toString().replace("\\-", "");
        application.setApplicationId(applicationId);
        application.setUserId(enrollRequest.getCommonRequest().getUserId());
        application.setCertificate(enrollRequest.getApplication());
        application.setProjectId(enrollRequest.getProjectId());
        application.setRelationId(ApplicationEnum.WAIT.getCode());
        applicationMapper.insert(application);
        List<ProjectUser> projectUserList = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, enrollRequest.getProjectId())
                        .eq(ProjectUser::getUserId, enrollRequest.getCommonRequest().getUserId())
        );
        int projectUserCount = ProjectUserMapper.selectCount(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, enrollRequest.getProjectId())
                        .eq(ProjectUser::getUserId, enrollRequest.getCommonRequest().getUserId())
        );
        if (projectUserCount > 0) {
            throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "请勿重复报名");
        }
        for (ProjectUser ProjectUser : projectUserList) {
            Project localProject = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, ProjectUser.getProjectId())
                            .eq(Project::getProjectIsLocal, IsLocalEnum.LOCAL.getCode())
            );
            if (localProject != null) {
                throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "只可以报名一个匠心项目");
            }
        }
    }

    @Override
    public Page<CheckEnrollResponse> checkAuditEnroll(CheckAuditEnrollRequest checkAuditEnrollRequest) {
        if (checkAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkAuditEnrollRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkAuditEnrollRequest.getCheckRequest().getCommonRequest().getToken())
                        .le(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您没有该权限！");
        }
        Page page = new Page(checkAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<CheckEnrollResponse> checkEnrollResponsePage = new Page<>(checkAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize());
        List<CheckEnrollResponse> checkEnrollResponseList = new ArrayList<>();
        Page<Application> applicationPage = applicationMapper.selectPage(
                page,
                Wrappers.lambdaQuery(Application.class)
                        .between(Application::getRelationId, ApplicationEnum.WAIT.getCode(), ApplicationEnum.RETEST_REFUSE.getCode())
                        .eq(Application::getBatchId, checkAuditEnrollRequest.getBatchId())
        );
        for (Application application : applicationPage.getRecords()) {
            CheckEnrollResponse checkEnrollResponse = new CheckEnrollResponse();
            checkEnrollResponse.setRelationId(application.getRelationId());
            if (Objects.equals(application.getRelationId(), ApplicationEnum.WAIT.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.WAIT.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.WAIT.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_PASS.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.FIRST_PASS.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.PASS.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_PASS.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.RETEST_PASS.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.PASS.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_REFUSE.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.FIRST_REFUSE.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.REFUSE.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_REFUSE.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.RETEST_REFUSE.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.REFUSE.getCode());
            }
            checkEnrollResponse.setEnrollId(application.getApplicationId());
            checkEnrollResponse.setCertificate(application.getCertificate());
            checkEnrollResponse.setUserId(application.getUserId());
            checkEnrollResponse.setProjectId(application.getProjectId());
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, application.getProjectId())
            );
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            checkEnrollResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkEnrollResponse.setProjectName(project.getProjectName().substring(index + 1));
            User traveller = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, application.getUserId())
            );
            checkEnrollResponse.setUserName(traveller.getUserName());
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, traveller.getInstituteId())
            );
            checkEnrollResponse.setInstitute(institute.getInstituteName());
            checkEnrollResponseList.add(checkEnrollResponse);
        }
        BeanUtils.copyProperties(applicationPage,checkEnrollResponsePage);
        checkEnrollResponseList.sort(CheckEnrollResponse.RelationId);
        checkEnrollResponsePage.setRecords(checkEnrollResponseList);
        return checkEnrollResponsePage;
    }

    @Override
    public void cancelEnroll(CancelEnrollRequest cancelEnrollRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, cancelEnrollRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, cancelEnrollRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败！");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, cancelEnrollRequest.getProjectId())
        );
        if (project == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "项目不存在");
        }
        Application application = applicationMapper.selectOne(
                Wrappers.lambdaQuery(Application.class)
                        .eq(Application::getProjectId, cancelEnrollRequest.getProjectId())
                        .eq(Application::getUserId, cancelEnrollRequest.getCommonRequest().getUserId())
        );
        if (application == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "您还没报名");
        } else if (!Objects.equals(application.getRelationId(), ApplicationEnum.WAIT.getCode())) {
            throw new CustomException(ErrorEnum.OVERDUE_ERROR.getCode(), "您的申请已被审核不能撤销。");
        }
        File file = new File(PathEnum.FilePath.getMsg() + "img/application/" + application.getCertificate());
        if (file.exists()) {
            file.delete();
            applicationMapper.deleteById(application);
        } else {
            throw new CustomException(ErrorEnum.FILE_DELETE_ERROR.getCode(), "文件删除失败");
        }
    }

    @Override
    public void auditApplication(AuditApplicationRequest auditApplicationRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, auditApplicationRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, auditApplicationRequest.getCommonRequest().getToken())
                        .le(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败！");
        }
        for (String applicationId : auditApplicationRequest.getApplicantId()) {
            Application application = applicationMapper.selectOne(
                    Wrappers.lambdaQuery(Application.class)
                            .eq(Application::getApplicationId, applicationId)
            );
            if (application == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "报名不存在");
            }
            String informId = "inform-" + UUID.randomUUID().toString().replace("\\-", "");
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, application.getProjectId())
            );
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            String projectSymbol = project.getProjectName().substring(0, index);
            String projectName = project.getProjectName().substring(index + 1);
            if (auditApplicationRequest.getIsPass().equals(PassEnum.PASS.getCode())) {
                application.setRelationId(ApplicationEnum.FIRST_PASS.getCode());
                applicationMapper.updateById(application);
                Inform inform = new Inform();
                inform.setInformId(informId);
                inform.setTitle("初试情况");
                inform.setSenderId(auditApplicationRequest.getCommonRequest().getUserId());
                inform.setAcceptorId(application.getUserId());
                inform.setInformation("您对项目：" + projectSymbol + "-" + projectName + "的报名申请初试已通过。请关注老师发布的复试通知。");
                informMapper.insert(inform);
            } else if (auditApplicationRequest.getIsPass().equals(PassEnum.REFUSE.getCode())) {
                application.setRelationId(ApplicationEnum.FIRST_REFUSE.getCode());
                applicationMapper.updateById(application);
                Inform inform = new Inform();
                inform.setInformId(informId);
                inform.setTitle("初试情况");
                inform.setSenderId(auditApplicationRequest.getCommonRequest().getUserId());
                inform.setAcceptorId(application.getUserId());
                inform.setInformation("您对项目：" + projectSymbol + "-" + projectName + "的报名申请初试未通过");
                informMapper.insert(inform);
                applicationMapper.deleteById(application);
                File localPath = new File("resource/img/application");
                File file = new File(localPath.getAbsolutePath() + "\\" + application.getCertificate());
                if (file.delete()) {
                    applicationMapper.deleteById(application);
                } else {
                    throw new CustomException(ErrorEnum.FILE_DELETE_ERROR.getCode(), "文件删除失败");
                }
            } else {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "状态码错误");
            }
        }
    }

    @Override
    public DownloadResponse downloadApplication(DownloadApplicationRequest downloadApplicationRequest) {
        DownloadResponse downloadResponse = new DownloadResponse();
        File localPath = new File(PathEnum.FilePath.getMsg() + "img/application/");
        downloadResponse.setFilePath(localPath.getAbsolutePath());
        Application application = applicationMapper.selectOne(
                Wrappers.lambdaQuery(Application.class)
                        .eq(Application::getApplicationId, downloadApplicationRequest.getApplicationId())
        );
        downloadResponse.setFileName(application.getCertificate());
        return downloadResponse;
    }

    @Override
    public Page<CheckEnrollResponse> searchAuditEnroll(SearchAuditEnrollRequest searchAuditEnrollRequest) {
        if (searchAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || searchAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchAuditEnrollRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, searchAuditEnrollRequest.getCheckRequest().getCommonRequest().getToken())
                        .le(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您没有该权限！");
        }
        Page page = new Page(searchAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage(), searchAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<CheckEnrollResponse> checkEnrollResponsePage = new Page<>(searchAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage(), searchAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize());
        List<CheckEnrollResponse> checkEnrollResponseList = new ArrayList<>();
        Page<Application> applicationPage = applicationMapper.selectPage(
                page,
                Wrappers.lambdaQuery(Application.class)
                        .eq(Application::getBatchId,searchAuditEnrollRequest.getBatchId())
                        .like(Application::getProjectId, searchAuditEnrollRequest.getProjectId())
                        .like(Application::getUserId, searchAuditEnrollRequest.getApplicantUserId())
                        .between(Application::getRelationId, ApplicationEnum.WAIT.getCode(), ApplicationEnum.RETEST_REFUSE.getCode())
        );
        for (Application application : applicationPage.getRecords()) {
            CheckEnrollResponse checkEnrollResponse = new CheckEnrollResponse();
            if (Objects.equals(application.getRelationId(), ApplicationEnum.WAIT.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.WAIT.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.WAIT.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_PASS.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.FIRST_PASS.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.PASS.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_PASS.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.RETEST_PASS.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.PASS.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_REFUSE.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.FIRST_REFUSE.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.REFUSE.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_REFUSE.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.RETEST_REFUSE.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.REFUSE.getCode());
            }
            checkEnrollResponse.setEnrollId(application.getApplicationId());
            checkEnrollResponse.setCertificate(application.getCertificate());
            checkEnrollResponse.setUserId(application.getUserId());
            checkEnrollResponse.setProjectId(application.getProjectId());
            checkEnrollResponse.setRelationId(application.getRelationId());
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, application.getProjectId())
                            .like(Project::getProjectName, searchAuditEnrollRequest.getProjectName())
                            .like(Project::getProjectName, searchAuditEnrollRequest.getProjectSymbol())
            );
            if (project == null) {
                continue;
            }
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            checkEnrollResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkEnrollResponse.setProjectName(project.getProjectName().substring(index + 1));
            User traveller = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, application.getUserId())
                            .like(User::getUserName, searchAuditEnrollRequest.getApplicantName())
            );
            if (traveller == null) {
                continue;
            }
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, traveller.getInstituteId())
            );
            checkEnrollResponse.setInstitute(institute.getInstituteName());
            checkEnrollResponse.setUserName(traveller.getUserName());
            checkEnrollResponseList.add(checkEnrollResponse);
        }
        checkEnrollResponseList.sort(CheckEnrollResponse.RelationId);
        checkEnrollResponsePage.setRecords(checkEnrollResponseList);
        BeanUtils.copyProperties(applicationPage,checkEnrollResponsePage);
        return checkEnrollResponsePage;
    }

    @Override
    public void auditRetest(AuditApplicationRequest auditApplicationRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, auditApplicationRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, auditApplicationRequest.getCommonRequest().getToken())
                        .le(User::getType, UserTypeEnum.TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败！");
        }
        for (String applicationId : auditApplicationRequest.getApplicantId()) {
            Application application = applicationMapper.selectOne(
                    Wrappers.lambdaQuery(Application.class)
                            .eq(Application::getApplicationId, applicationId)
                            .eq(Application::getRelationId, ApplicationEnum.FIRST_PASS.getCode())
            );
            if (application == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "报名不存在");
            }
            String informId = "inform-" + UUID.randomUUID().toString().replace("\\-", "");
            String projectUserId = "PU-" + UUID.randomUUID().toString().replace("\\-", "");
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, application.getProjectId())
            );
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            String projectSymbol = project.getProjectName().substring(0, index);
            String projectName = project.getProjectName().substring(index + 1);
            ProjectUser ProjectUser = new ProjectUser();
            BatchUser batchUser = new BatchUser();
            if (auditApplicationRequest.getIsPass().equals(PassEnum.PASS.getCode())) {
                application.setRelationId(ApplicationEnum.RETEST_PASS.getCode());
                ProjectUser.setRelationId(projectUserId);
                ProjectUser.setUserId(application.getUserId());
                ProjectUser.setType(ProjectUserRelationEnum.PARTNER.getCode());
                ProjectUser.setProjectId(application.getProjectId());
                String batchUserId = "BU-" + UUID.randomUUID().toString().replace("\\-", "");
                batchUser.setRelationId(batchUserId);
                batchUser.setUserId(application.getUserId());
                BatchProject batchProject = batchProjectMapper.selectOne(
                        Wrappers.lambdaQuery(BatchProject.class)
                                .eq(BatchProject::getProjectId,application.getProjectId())
                );
                batchUser.setBatchId(batchProject.getBatchId());
                applicationMapper.updateById(application);
                ProjectUserMapper.insert(ProjectUser);
                Inform inform = new Inform();
                inform.setInformId(informId);
                inform.setTitle("复试情况");
                inform.setSenderId(auditApplicationRequest.getCommonRequest().getUserId());
                inform.setAcceptorId(application.getUserId());
                inform.setInformation("您对项目：" + projectSymbol + "-" + projectName + "的复试已通过，恭喜您成功参与了该项目。");
                informMapper.insert(inform);
            } else if (auditApplicationRequest.getIsPass().equals(PassEnum.REFUSE.getCode())) {
                application.setRelationId(ApplicationEnum.RETEST_REFUSE.getCode());
                Inform inform = new Inform();
                inform.setInformId(informId);
                inform.setTitle("复试情况");
                inform.setSenderId(auditApplicationRequest.getCommonRequest().getUserId());
                inform.setAcceptorId(application.getUserId());
                inform.setInformation("您对项目：" + projectSymbol + "-" + projectName + "的报名申请复试未通过，请不要灰心，接下来还有调剂环节。");
                informMapper.insert(inform);
                applicationMapper.updateById(application);
                applicationMapper.deleteById(application);
                File localPath = new File("resource/img/application");
                File file = new File(localPath.getAbsolutePath() + "\\" + application.getCertificate());
                if (file.delete()) {
                    applicationMapper.deleteById(application);
                } else {
                    throw new CustomException(ErrorEnum.FILE_DELETE_ERROR.getCode(), "文件删除失败");
                }
            } else {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "状态码错误");
            }
        }
    }

    @Override
    public PageUtil<CheckEnrollResponse> checkAuditRetest(CheckAuditEnrollRequest checkAuditEnrollRequest) {
        if (checkAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkAuditEnrollRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkAuditEnrollRequest.getCheckRequest().getCommonRequest().getToken())
                        .le(User::getType, UserTypeEnum.TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败！");
        }
        List<CheckEnrollResponse> checkEnrollResponseList = new ArrayList<>();
        List<ProjectUser> ProjectUsers = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getUserId, user.getUserId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
        );
        for (ProjectUser projectUser : ProjectUsers) {
            List<Application> applicationList = applicationMapper.selectList(
                    Wrappers.lambdaQuery(Application.class)
                            .eq(Application::getProjectId, projectUser.getProjectId())
                            .between(Application::getRelationId, ApplicationEnum.FIRST_PASS.getCode(), ApplicationEnum.RETEST_PASS.getCode())
            );
            for (Application application : applicationList) {
                BatchProject batchProject = batchProjectMapper.selectOne(
                        Wrappers.lambdaQuery(BatchProject.class)
                                .eq(BatchProject::getBatchId, checkAuditEnrollRequest.getBatchId())
                                .eq(BatchProject::getProjectId, application.getProjectId())
                );
                if (batchProject == null) {
                    continue;
                }
                CheckEnrollResponse checkEnrollResponse = new CheckEnrollResponse();
                checkEnrollResponse.setRelationId(application.getRelationId());
                if (Objects.equals(application.getRelationId(), ApplicationEnum.WAIT.getCode())) {
                    checkEnrollResponse.setEnrollStage(ApplicationEnum.WAIT.getMsg());
                    checkEnrollResponse.setIsPass(ApplicationIsPassEnum.WAIT.getCode());
                } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_PASS.getCode())) {
                    checkEnrollResponse.setEnrollStage(ApplicationEnum.FIRST_PASS.getMsg());
                    checkEnrollResponse.setIsPass(ApplicationIsPassEnum.PASS.getCode());
                } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_PASS.getCode())) {
                    checkEnrollResponse.setEnrollStage(ApplicationEnum.RETEST_PASS.getMsg());
                    checkEnrollResponse.setIsPass(ApplicationIsPassEnum.PASS.getCode());
                } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_REFUSE.getCode())) {
                    checkEnrollResponse.setEnrollStage(ApplicationEnum.FIRST_REFUSE.getMsg());
                    checkEnrollResponse.setIsPass(ApplicationIsPassEnum.REFUSE.getCode());
                } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_REFUSE.getCode())) {
                    checkEnrollResponse.setEnrollStage(ApplicationEnum.RETEST_REFUSE.getMsg());
                    checkEnrollResponse.setIsPass(ApplicationIsPassEnum.REFUSE.getCode());
                }
                checkEnrollResponse.setEnrollId(application.getApplicationId());
                checkEnrollResponse.setCertificate(application.getCertificate());
                checkEnrollResponse.setUserId(application.getUserId());
                checkEnrollResponse.setProjectId(application.getProjectId());
                Project project = projectMapper.selectOne(
                        Wrappers.lambdaQuery(Project.class)
                                .eq(Project::getProjectId, application.getProjectId())
                );
                int index = project.getProjectName().lastIndexOf('_');
                if (index == -1) {
                    throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
                }
                checkEnrollResponse.setProjectSymbol(project.getProjectName().substring(0, index));
                checkEnrollResponse.setProjectName(project.getProjectName().substring(index + 1));
                User traveller = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, application.getUserId())
                );
                checkEnrollResponse.setUserName(traveller.getUserName());
                Institute institute = instituteMapper.selectOne(
                        Wrappers.lambdaQuery(Institute.class)
                                .eq(Institute::getInstituteId, traveller.getInstituteId())
                );
                checkEnrollResponse.setInstitute(institute.getInstituteName());
                checkEnrollResponseList.add(checkEnrollResponse);
            }
        }
        checkEnrollResponseList.sort(CheckEnrollResponse.RelationId);
        PageUtil<CheckEnrollResponse> checkEnrollResponsePageUtil = new PageUtil<>(checkAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize(), checkEnrollResponseList);
        return checkEnrollResponsePageUtil;
    }

    @Override
    public Page<ShowAllApplicationResponse> showAllApplicationProject(ShowAllApplicationRequest showAllApplicationRequest) {
        if (showAllApplicationRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || showAllApplicationRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, showAllApplicationRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, showAllApplicationRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, showAllApplicationRequest.getBatchId())
                        .le(Batch::getEnrollBeginning, LocalDateTime.now())
                        .ge(Batch::getEnrollDeadline, LocalDateTime.now())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "当前批次未处于报名阶段");
        }
        List<ProjectUser> ProjectUsers;
        List<ProjectMajor> ProjectMajors;
        Page page = new Page(showAllApplicationRequest.getCheckRequest().getPageRequest().getCurrentPage(), showAllApplicationRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<ShowAllApplicationResponse> showAllApplicationResponsePage = new Page<>(showAllApplicationRequest.getCheckRequest().getPageRequest().getCurrentPage(), showAllApplicationRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<Project> projectPage = projectMapper.selectPage(
                page,
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getBatchId,batch.getBatchId())
                        .eq(Project::getProjectIsLocal, showAllApplicationRequest.getProjectType())
        );
        if (projectPage.getRecords().size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到处于征集阶段的项目");
        }
        List<ShowAllApplicationResponse> showAllApplicationResponses = new ArrayList<>();
        for (Project project : projectPage.getRecords()) {
            User user;
            Major major;
            List<String> teachers = new ArrayList<>();
            List<String> majors = new ArrayList<>();
            ShowAllApplicationResponse showAllApplicationResponse = new ShowAllApplicationResponse();
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            showAllApplicationResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            showAllApplicationResponse.setProjectName(project.getProjectName().substring(index + 1));
            showAllApplicationResponse.setProjectId(project.getProjectId());
            showAllApplicationResponse.setBatchName(batch.getBatchName());
            showAllApplicationResponse.setProjectOrigin(project.getProjectOrigin());
            showAllApplicationResponse.setProjectIntroduction(project.getProjectInstruction());
            ProjectUsers = ProjectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, project.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
            );
            ProjectMajors = ProjectMajorMapper.selectList(
                    Wrappers.lambdaQuery(ProjectMajor.class)
                            .eq(ProjectMajor::getProjectId, project.getProjectId())

            );
            for (ProjectUser ProjectUser : ProjectUsers) {
                user = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, ProjectUser.getUserId())
                );
                if (user == null) {
                    throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "指导老师不存在");
                }
                teachers.add(user.getUserName());
            }
            for (ProjectMajor ProjectMajor : ProjectMajors) {
                major = majorMapper.selectOne(
                        Wrappers.lambdaQuery(Major.class)
                                .eq(Major::getMajorId, ProjectMajor.getMajorId())
                );
                if (major == null) {
                    throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "专业不存在");
                }
                majors.add(major.getMajorName());
            }
            if (majors.size() == 0) {
                majors.add("任意专业");
            }
            if (teachers.size() == 0) {
                teachers.add("暂无指导老师");
            }
            ProjectUser projectUser = ProjectUserMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, project.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.APPLICANT.getCode())
            );
            if (projectUser == null) {
                throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "数据库数据缺失");
            }
            showAllApplicationResponse.setApplicantId(projectUser.getUserId());
            showAllApplicationResponse.setProjectTeacher(teachers);
            showAllApplicationResponse.setProjectNeedMajor(majors);
            showAllApplicationResponse.setProjectNeedPeople(project.getProjectNeedNumber());
            showAllApplicationResponses.add(showAllApplicationResponse);
        }
        for (ShowAllApplicationResponse showAllApplicationResponse : showAllApplicationResponses) {
            List<Application> applicationList = applicationMapper.selectList(
                    Wrappers.lambdaQuery(Application.class)
                            .eq(Application::getUserId, showAllApplicationRequest.getCheckRequest().getCommonRequest().getUserId())
                            .eq(Application::getProjectId, showAllApplicationResponse.getProjectId())
            );
            for (Application application : applicationList) {
                String realtion = null;
                if (Objects.equals(application.getRelationId(), ApplicationEnum.WAIT.getCode())) {
                    realtion = ApplicationEnum.WAIT.getMsg();
                } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_PASS.getCode())) {
                    realtion = ApplicationEnum.FIRST_PASS.getMsg();
                } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_PASS.getCode())) {
                    realtion = ApplicationEnum.RETEST_PASS.getMsg();
                } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_REFUSE.getCode())) {
                    realtion = ApplicationEnum.FIRST_REFUSE.getMsg();
                } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_REFUSE.getCode())) {
                    realtion = ApplicationEnum.RETEST_REFUSE.getMsg();
                }
                showAllApplicationResponse.setRelationId(realtion);
            }
        }
        BeanUtils.copyProperties(projectPage,showAllApplicationResponsePage);
        showAllApplicationResponsePage.setRecords(showAllApplicationResponses);
        return showAllApplicationResponsePage;
    }

    @Override
    public Page<ShowAllApplicantResponse> showAllApplicant(ShowAllApplicantRequest showAllApplicantRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, showAllApplicantRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, showAllApplicantRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Page page = new Page(showAllApplicantRequest.getCheckRequest().getPageRequest().getCurrentPage(),showAllApplicantRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<ShowAllApplicantResponse> showAllApplicantResponsePage = new Page<>(showAllApplicantRequest.getCheckRequest().getPageRequest().getCurrentPage(), showAllApplicantRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<ProjectUser> projectUserPage = ProjectUserMapper.selectPage(
                page,
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, showAllApplicantRequest.getProjectId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
        );
        List<ShowAllApplicantResponse> showAllApplicantResponses = new ArrayList<>();
        for (ProjectUser ProjectUser : projectUserPage.getRecords()) {
            ShowAllApplicantResponse showAllApplicantResponse = new ShowAllApplicantResponse();
            User userA = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, ProjectUser.getUserId())
            );
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, userA.getInstituteId())
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
                            .eq(Major::getMajorId, userA.getMajorId())
                            .eq(Major::getMajorIsDel, IsDelEnum.UNDELETE.getCode())
            );
            if (major == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "专业不存在");
            }
            Application application = applicationMapper.selectOne(
                    Wrappers.lambdaQuery(Application.class)
                            .eq(Application::getUserId, userA.getUserId())
                            .eq(Application::getProjectId, showAllApplicantRequest.getProjectId())
            );
            String applicationStage = null;
            if (Objects.equals(application.getRelationId(), ApplicationEnum.WAIT.getCode())) {
                applicationStage = ApplicationEnum.WAIT.getMsg();
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_PASS.getCode())) {
                applicationStage = ApplicationEnum.FIRST_PASS.getMsg();
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_PASS.getCode())) {
                applicationStage = ApplicationEnum.RETEST_PASS.getMsg();
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_REFUSE.getCode())) {
                applicationStage = ApplicationEnum.FIRST_REFUSE.getMsg();
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_REFUSE.getCode())) {
                applicationStage = ApplicationEnum.RETEST_REFUSE.getMsg();
            }
            showAllApplicantResponse.setUserId(userA.getUserId());
            showAllApplicantResponse.setUserName(userA.getUserName());
            showAllApplicantResponse.setEmail(userA.getEMail());
            showAllApplicantResponse.setInstitute(institute.getInstituteName());
            showAllApplicantResponse.setMajor(major.getMajorName());
            showAllApplicantResponse.setType(userA.getType());
            showAllApplicantResponse.setApplicantId(application.getApplicationId());
            showAllApplicantResponse.setIsAudit(applicationStage);
            showAllApplicantResponses.add(showAllApplicantResponse);
        }
        showAllApplicantResponsePage.setRecords(showAllApplicantResponses);
        BeanUtils.copyProperties(projectUserPage,showAllApplicantResponsePage);
        return showAllApplicantResponsePage;
    }

    @Override
    public PageUtil<CheckEnrollResponse> searchAuditRetest(SearchAuditEnrollRequest searchAuditEnrollRequest) {
        if (searchAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || searchAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchAuditEnrollRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, searchAuditEnrollRequest.getCheckRequest().getCommonRequest().getToken())
                        .le(User::getType, UserTypeEnum.TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您没有该权限！");
        }
        List<CheckEnrollResponse> checkEnrollResponseList = new ArrayList<>();
        List<Application> applicationList = applicationMapper.selectList(
                Wrappers.lambdaQuery(Application.class)
                        .like(Application::getProjectId, searchAuditEnrollRequest.getProjectId())
                        .like(Application::getUserId, searchAuditEnrollRequest.getApplicantUserId())
                        .eq(Application::getRelationId, ApplicationEnum.FIRST_PASS.getCode()).or()
                        .eq(Application::getRelationId, ApplicationEnum.RETEST_PASS.getCode()).or()
                        .eq(Application::getRelationId, ApplicationEnum.RETEST_REFUSE.getCode())
        );
        for (Application application : applicationList) {
            BatchProject batchProject = batchProjectMapper.selectOne(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getBatchId, searchAuditEnrollRequest.getBatchId())
                            .eq(BatchProject::getProjectId, application.getProjectId())
            );
            if (batchProject == null) {
                continue;
            }
            CheckEnrollResponse checkEnrollResponse = new CheckEnrollResponse();
            if (Objects.equals(application.getRelationId(), ApplicationEnum.WAIT.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.WAIT.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.WAIT.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_PASS.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.FIRST_PASS.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.PASS.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_PASS.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.RETEST_PASS.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.PASS.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.FIRST_REFUSE.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.FIRST_REFUSE.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.REFUSE.getCode());
            } else if (Objects.equals(application.getRelationId(), ApplicationEnum.RETEST_REFUSE.getCode())) {
                checkEnrollResponse.setEnrollStage(ApplicationEnum.RETEST_REFUSE.getMsg());
                checkEnrollResponse.setIsPass(ApplicationIsPassEnum.REFUSE.getCode());
            }
            checkEnrollResponse.setEnrollId(application.getApplicationId());
            checkEnrollResponse.setCertificate(application.getCertificate());
            checkEnrollResponse.setUserId(application.getUserId());
            checkEnrollResponse.setProjectId(application.getProjectId());
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, application.getProjectId())
                            .like(Project::getProjectName, searchAuditEnrollRequest.getProjectName())
                            .like(Project::getProjectName, searchAuditEnrollRequest.getProjectSymbol())
            );
            if (project == null) {
                continue;
            }
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            checkEnrollResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkEnrollResponse.setProjectName(project.getProjectName().substring(index + 1));
            User traveller = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, application.getUserId())
                            .like(User::getUserName, searchAuditEnrollRequest.getApplicantName())
            );
            if (traveller == null) {
                continue;
            }
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, traveller.getInstituteId())
            );
            checkEnrollResponse.setInstitute(institute.getInstituteName());
            checkEnrollResponse.setUserName(traveller.getUserName());
            checkEnrollResponseList.add(checkEnrollResponse);
        }
        checkEnrollResponseList.sort(CheckEnrollResponse.RelationId);
        PageUtil<CheckEnrollResponse> checkEnrollResponsePageUtil = new PageUtil<>(searchAuditEnrollRequest.getCheckRequest().getPageRequest().getCurrentPage(), searchAuditEnrollRequest.getCheckRequest().getPageRequest().getPageSize(), checkEnrollResponseList);
        return checkEnrollResponsePageUtil;
    }

    @Override
    public DownloadResponse downloadApplicationTemplate(DownloadTemplateRequest downloadTemplateRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, downloadTemplateRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, downloadTemplateRequest.getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "身份验证失败");
        }
        DownloadResponse downloadResponse = new DownloadResponse();
        File localPath = new File(PathEnum.FilePath.getMsg() + "template/user");
        File[] array = localPath.listFiles();
        downloadResponse.setFilePath(localPath.getAbsolutePath());
        downloadResponse.setFileName(array[0].getName());
        return downloadResponse;
    }
}
