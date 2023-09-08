package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.*;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.mapper.*;
import com.Hebut.JiangXin.project.service.IProjectUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 项目用户关系表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2021-12-29
 */
@Service
public class ProjectUserServiceImpl extends ServiceImpl<ProjectUserMapper, ProjectUser> implements IProjectUserService {

    @Resource
    ProjectUserMapper ProjectUserMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    InstituteMapper instituteMapper;
    @Resource
    MajorMapper majorMapper;
    @Resource
    InstituteMajorMapper InstituteMajorMapper;
    @Resource
    ProjectMajorMapper ProjectMajorMapper;
    @Resource
    ApplicationMapper applicationMapper;
    @Resource
    BatchMapper batchMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    ExpenseMapper expenseMapper;

    @Override
    public SearchResultResponse searchResult(CommonRequest commonRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, commonRequest.getUserId())
                        .eq(User::getToken, commonRequest.getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您的账号在异地登录");
        }
        SearchResultResponse searchResultResponse = new SearchResultResponse();
        List<ProjectUser> projectUser = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getUserId, user.getUserId())
        );
        if (projectUser.size() == 0) {
            throw new CustomException(ErrorEnum.MISMATCH_ERROR.getCode(), "您未申请加入任何项目");
        }
        Project projects = new Project();
        ProjectUser relation = new ProjectUser();
        for (ProjectUser ProjectUsers : projectUser) {
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, ProjectUsers.getProjectId())
                            .eq(Project::getProjectIsLocal, IsLocalEnum.LOCAL.getCode())
            );
            if (project != null) {
                projects = project;
                relation = ProjectUsers;
                break;
            }
        }
        if (projects.getId() == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "暂时无报名的匠心项目");
        }
        searchResultResponse.setProjectId(projects.getProjectId());
        int index = projects.getProjectName().lastIndexOf('_');
        if (index == -1) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
        }
        searchResultResponse.setProjectSymbol(projects.getProjectName().substring(0, index));
        searchResultResponse.setProjectName(projects.getProjectName().substring(index + 1));
        searchResultResponse.setNowRelation(relation.getRelationId());
        return searchResultResponse;
    }

    @Override
    public List<SearchPartnerResponse> searchPartner(ScanProjectRequest scanProjectRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, scanProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, scanProjectRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        List<ProjectUser> ProjectUsers = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, scanProjectRequest.getProjectId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
        );
        List<SearchPartnerResponse> searchPartnerResponses = new ArrayList<>();
        for (ProjectUser ProjectUser : ProjectUsers) {
            SearchPartnerResponse searchPartnerResponse = new SearchPartnerResponse();
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
                            .eq(Application::getProjectId, scanProjectRequest.getProjectId())
                            .eq(Application::getUserId, userA.getUserId())
                            .eq(Application::getRelationId, ApplicationEnum.RETEST_PASS.getCode())
            );
            if (application == null) {
                continue;
            }
            searchPartnerResponse.setApplicationId(application.getApplicationId());
            searchPartnerResponse.setUserId(userA.getUserId());
            searchPartnerResponse.setUserName(userA.getUserName());
            searchPartnerResponse.setEmail(userA.getEMail());
            searchPartnerResponse.setInstitute(institute.getInstituteName());
            searchPartnerResponse.setMajor(major.getMajorName());
            searchPartnerResponse.setType(userA.getType());
            searchPartnerResponses.add(searchPartnerResponse);
        }
        return searchPartnerResponses;
    }

    @Override
    public List<InformationResponse> searchTeacher(CommonRequest commonRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, commonRequest.getUserId())
                        .eq(User::getToken, commonRequest.getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        List<ProjectUser> ProjectUsers = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getUserId, commonRequest.getUserId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
        );
        List<InformationResponse> informationResponses = new ArrayList<>();
        for (ProjectUser projectUser : ProjectUsers) {
            List<ProjectUser> projectUserList = ProjectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, projectUser.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
            );
            for (ProjectUser userRelation : projectUserList) {
                InformationResponse informationResponse = new InformationResponse();
                User teacher = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, userRelation.getUserId())
                );
                Institute institute = instituteMapper.selectOne(
                        Wrappers.lambdaQuery(Institute.class)
                                .eq(Institute::getInstituteId, teacher.getInstituteId())
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
                                .eq(Major::getMajorId, teacher.getMajorId())
                                .eq(Major::getMajorIsDel, IsDelEnum.UNDELETE.getCode())
                );
                if (major == null) {
                    throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "专业不存在");
                }
                informationResponse.setUserId(teacher.getUserId());
                informationResponse.setUserName(teacher.getUserName());
                informationResponse.setEmail(teacher.getEMail());
                informationResponse.setInstitute(institute.getInstituteName());
                informationResponse.setMajor(major.getMajorName());
                informationResponse.setType(teacher.getType());
                if (informationResponses.contains(informationResponse)) {
                    continue;
                }
                informationResponses.add(informationResponse);
            }

        }
        return informationResponses;
    }

    @Override
    public PageUtil<ShowProjectResponse> searchMyProject(SearchMyProjectRequest searchMyProjectRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchMyProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, searchMyProjectRequest.getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<ProjectUser> ProjectUserList = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getUserId, searchMyProjectRequest.getCommonRequest().getUserId())
                        .le(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
                        .ge(ProjectUser::getType, ProjectUserRelationEnum.APPLICANT.getCode())
        );
        List<String> projectId = new ArrayList<>();
        List<Batch> batchList = batchMapper.selectList(
                Wrappers.lambdaQuery(Batch.class)
        );
        if (batchList.size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "暂无批次");
        }
        Batch batch = batchList.get(0);
        for (int i = 1; i < batchList.size(); i++) {
            if (batchList.get(i).getDefendDeadline().isAfter(batchList.get(i - 1).getDefendDeadline())) {
                batch = batchList.get(i);
            }
        }
        for (ProjectUser userRelation : ProjectUserList) {
            if (!projectId.contains(userRelation.getProjectId())) {
                projectId.add(userRelation.getProjectId());
            }
        }
        List<ShowProjectResponse> showProjectResponseList = new ArrayList<>();
        for (String projectI : projectId) {
            ShowProjectResponse showProjectResponse = new ShowProjectResponse();
            List<String> teachers = new ArrayList<>();
            List<String> majors = new ArrayList<>();
            showProjectResponse.setProjectId(projectI);
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, projectI)
            );
            if (project == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "项目不存在");
            }
            if (!project.getProjectIsLocal().equals(searchMyProjectRequest.getProjectType())) {
                continue;
            }
            showProjectResponse.setRelationId(projectI);
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            showProjectResponse.setBatchName(batch.getBatchName());
            showProjectResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            showProjectResponse.setProjectName(project.getProjectName().substring(index + 1));
            showProjectResponse.setProjectOrigin(project.getProjectOrigin());
            showProjectResponse.setProjectNeedPeople(project.getProjectNeedNumber());
            showProjectResponse.setProjectIntroduction(project.getProjectInstruction());
            showProjectResponse.setAudit(project.getStage());
            String stage = null;
            if (LocalDateTime.now().isAfter(batch.getEnrollBeginning()) && LocalDateTime.now().isBefore(batch.getEnrollDeadline())) {
                stage = ProjectStageEnum.SELECT.getCode();
            } else if (LocalDateTime.now().isAfter(batch.getMediumBeginning()) && LocalDateTime.now().isBefore(batch.getMediumDeadline())) {
                stage = ProjectStageEnum.MEDIUM.getCode();
            } else if (LocalDateTime.now().isAfter(batch.getDefendBeginning()) && LocalDateTime.now().isBefore(batch.getDefendDeadline())) {
                stage = ProjectStageEnum.PRESENTATION.getCode();
            } else if (LocalDateTime.now().isAfter(batch.getDefendDeadline())) {
                stage = ProjectStageEnum.ENDING.getCode();
            }
            showProjectResponse.setStage(stage);
            List<ProjectUser> ProjectUsers = ProjectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, projectI)
                            .eq(ProjectUser::getRelationId, ProjectUserRelationEnum.TEACHER.getCode())
            );
            for (ProjectUser ProjectUser : ProjectUsers) {
                User teacher = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, ProjectUser.getUserId())
                );
                teachers.add(teacher.getUserName());
            }
            List<ProjectMajor> ProjectMajors = ProjectMajorMapper.selectList(
                    Wrappers.lambdaQuery(ProjectMajor.class)
                            .eq(ProjectMajor::getProjectId, projectI)
            );
            for (int j = 0; j < ProjectUsers.size(); j++) {
                Major major = majorMapper.selectOne(
                        Wrappers.lambdaQuery(Major.class)
                                .eq(Major::getMajorId, ProjectMajors.get(j).getMajorId())
                );
                majors.add(major.getMajorName());
            }
            showProjectResponse.setProjectTeacher(teachers);
            showProjectResponse.setProjectNeedMajor(majors);
            showProjectResponseList.add(showProjectResponse);
        }
        PageUtil<ShowProjectResponse> showProjectResponsePage = new PageUtil<>(searchMyProjectRequest.getPageRequest().getCurrentPage(), searchMyProjectRequest.getPageRequest().getPageSize(), showProjectResponseList);
        return showProjectResponsePage;
    }

    @Override
    public ScanProjectResponse localProject(CommonRequest commonRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, commonRequest.getUserId())
                        .eq(User::getToken, commonRequest.getToken())
                        .eq(User::getType, UserTypeEnum.STUDENT.getCode())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<Batch> batchList = batchMapper.selectList(
                Wrappers.lambdaQuery(Batch.class)
        );
        if (batchList.size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "暂无批次");
        }
        Batch batch = batchList.get(0);
        for (int i = 1; i < batchList.size(); i++) {
            if (batchList.get(i).getDefendDeadline().isAfter(batchList.get(i - 1).getDefendDeadline())) {
                batch = batchList.get(i);
            }
        }
        String localProject = null;
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, batch.getBatchId())
        );
        for (BatchProject batchProject : batchProjectList) {
            ProjectUser projectUser = ProjectUserMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getUserId, token.getUserId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
                            .eq(ProjectUser::getProjectId, batchProject.getProjectId())
            );
            if (projectUser != null) {
                localProject = projectUser.getProjectId();
            }
        }

        if (localProject == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "您还没有参加匠心项目");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, localProject)
        );

        ScanProjectResponse scanProjectResponse = new ScanProjectResponse();
        scanProjectResponse.setProjectId(project.getProjectId());
        int index = project.getProjectName().lastIndexOf('_');
        if (index == -1) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
        }
        scanProjectResponse.setProjectSymbol(project.getProjectName().substring(0, index));
        scanProjectResponse.setProjectName(project.getProjectName().substring(index + 1));
        scanProjectResponse.setProjectOrigin(project.getProjectOrigin());
        scanProjectResponse.setProjectPeople(project.getProjectNeedNumber());
        scanProjectResponse.setAudit(project.getStage());
        String stage = null;
        if (LocalDateTime.now().isAfter(batch.getEnrollBeginning()) && LocalDateTime.now().isBefore(batch.getEnrollDeadline())) {
            stage = ProjectStageEnum.SELECT.getCode();
        } else if (LocalDateTime.now().isAfter(batch.getMediumBeginning()) && LocalDateTime.now().isBefore(batch.getMediumDeadline())) {
            stage = ProjectStageEnum.MEDIUM.getCode();
        } else if (LocalDateTime.now().isAfter(batch.getDefendBeginning()) && LocalDateTime.now().isBefore(batch.getDefendDeadline())) {
            stage = ProjectStageEnum.PRESENTATION.getCode();
        } else if (LocalDateTime.now().isAfter(batch.getDefendDeadline())) {
            stage = ProjectStageEnum.ENDING.getCode();
        }
        scanProjectResponse.setStage(stage);
        User user;
        Major major;
        List<String> teachers = new ArrayList<>();
        List<String> majors = new ArrayList<>();
        List<ProjectUser> ProjectUsers = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, project.getProjectId())
                        .eq(ProjectUser::getRelationId, ProjectUserRelationEnum.TEACHER.getCode())
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
        List<ProjectMajor> ProjectMajors = ProjectMajorMapper.selectList(
                Wrappers.lambdaQuery(ProjectMajor.class)
                        .eq(ProjectMajor::getProjectId, project.getProjectId())

        );
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
        scanProjectResponse.setTeachers(teachers);
        scanProjectResponse.setMajors(majors);
        File localPath = new File("resource/prospectus");
        scanProjectResponse.setFilePath(localPath.getAbsolutePath() + "\\");
        scanProjectResponse.setFileName(project.getProjectInstruction());
        return scanProjectResponse;
    }

    @Override
    public PageUtil<SearchGuideProjectResponse> searchGuideProject(SearchGuideProjectRequest searchGuideProjectRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchGuideProjectRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, searchGuideProjectRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, searchGuideProjectRequest.getBatchId())
        );
        List<ProjectUser> projectUserList = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getBatchId, searchGuideProjectRequest.getBatchId())
                        .eq(ProjectUser::getUserId, searchGuideProjectRequest.getTeacherId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
        );
        List<SearchGuideProjectResponse> searchGuideProjectResponses = new ArrayList<>();
        for (ProjectUser projectUser : projectUserList) {
            SearchGuideProjectResponse searchGuideProjectResponse = new SearchGuideProjectResponse();
            List<String> teachers = new ArrayList<>();
            List<String> majors = new ArrayList<>();
            searchGuideProjectResponse.setProjectId(projectUser.getProjectId());
            searchGuideProjectResponse.setBatchName(batch.getBatchName());
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, projectUser.getProjectId())
            );
            if (project == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "项目不存在");
            }
            if (!project.getProjectIsLocal().equals(searchGuideProjectRequest.getProjectType())) {
                continue;
            }
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            searchGuideProjectResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            searchGuideProjectResponse.setProjectName(project.getProjectName().substring(index + 1));
            searchGuideProjectResponse.setProjectOrigin(project.getProjectOrigin());
            searchGuideProjectResponse.setProjectNeedPeople(project.getProjectNeedNumber());
            searchGuideProjectResponse.setProjectIntroduction(project.getProjectInstruction());
            searchGuideProjectResponse.setAudit(project.getStage());
            String stage = null;
            if (LocalDateTime.now().isAfter(batch.getEnrollBeginning()) && LocalDateTime.now().isBefore(batch.getEnrollDeadline())) {
                stage = ProjectStageEnum.SELECT.getCode();
            } else if (LocalDateTime.now().isAfter(batch.getMediumBeginning()) && LocalDateTime.now().isBefore(batch.getMediumDeadline())) {
                stage = ProjectStageEnum.MEDIUM.getCode();
            } else if (LocalDateTime.now().isAfter(batch.getDefendBeginning()) && LocalDateTime.now().isBefore(batch.getDefendDeadline())) {
                stage = ProjectStageEnum.PRESENTATION.getCode();
            } else if (LocalDateTime.now().isAfter(batch.getDefendDeadline())) {
                stage = ProjectStageEnum.ENDING.getCode();
            }
            searchGuideProjectResponse.setStage(stage);
            List<ProjectUser> ProjectUsers = ProjectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, projectUser.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
            );
            for (ProjectUser ProjectUser : ProjectUsers) {
                User teacher = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, ProjectUser.getUserId())
                );
                teachers.add(teacher.getUserName());
            }
            List<ProjectMajor> ProjectMajors = ProjectMajorMapper.selectList(
                    Wrappers.lambdaQuery(ProjectMajor.class)
                            .eq(ProjectMajor::getProjectId, projectUser.getProjectId())
            );
            for (int j = 0; j < ProjectMajors.size(); j++) {
                Major major = majorMapper.selectOne(
                        Wrappers.lambdaQuery(Major.class)
                                .eq(Major::getMajorId, ProjectMajors.get(j).getMajorId())
                );
                majors.add(major.getMajorName());
            }
            searchGuideProjectResponse.setProjectTeacher(teachers);
            searchGuideProjectResponse.setProjectNeedMajor(majors);
            List<Expense> expenseList = expenseMapper.selectList(
                    Wrappers.lambdaQuery(Expense.class)
                            .eq(Expense::getProjectId, project.getProjectId())
                            .eq(Expense::getIsPass, ExpenseEnum.PASSING.getCode())
            );
            BigDecimal expenseAmount = new BigDecimal(0);
            for (Expense expense : expenseList) {
                expenseAmount = expenseAmount.add(expense.getAmount());
            }
            searchGuideProjectResponse.setAmount(expenseAmount);
            searchGuideProjectResponses.add(searchGuideProjectResponse);

        }
        PageUtil<SearchGuideProjectResponse> searchGuideProjectResponsePageUtil = new PageUtil<>(searchGuideProjectRequest.getCheckRequest().getPageRequest().getCurrentPage(), searchGuideProjectRequest.getCheckRequest().getPageRequest().getPageSize(), searchGuideProjectResponses);
        return searchGuideProjectResponsePageUtil;
    }

    @Override
    public void distributeProject(DistributeProjectRequest distributeProjectRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, distributeProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, distributeProjectRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        User expert = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, distributeProjectRequest.getExpertId())
                        .eq(User::getType, UserTypeEnum.EXPERT.getCode())
        );
        if (expert == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "分配任务的专家不存在");
        }
        for (String projectId : distributeProjectRequest.getProjectIdList()) {
            ProjectUser repeat = ProjectUserMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, projectId)
                            .eq(ProjectUser::getUserId, expert.getUserId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.EXPERT.getCode())
            );
            if (repeat != null) {
                throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "已为该专家分配该项目");
            }
            ProjectUser ProjectUser = new ProjectUser();
            String relationIdPu = "PU-" + UUID.randomUUID().toString().replace("\\-", "");
            ProjectUser.setProjectId(projectId);
            ProjectUser.setType(ProjectUserRelationEnum.EXPERT.getCode());
            ProjectUser.setUserId(expert.getUserId());
            ProjectUser.setRelationId(relationIdPu);
            ProjectUserMapper.insert(ProjectUser);
        }
    }

    @Override
    public ScanProjectResponse futureProject(CommonRequest commonRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, commonRequest.getUserId())
                        .eq(User::getToken, commonRequest.getToken())
                        .eq(User::getType, UserTypeEnum.STUDENT.getCode())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<Application> applicationList = applicationMapper.selectList(
                Wrappers.lambdaQuery(Application.class)
                        .eq(Application::getUserId, token.getUserId())
                        .between(Application::getRelationId, ApplicationEnum.WAIT.getCode(), ApplicationEnum.RETEST_PASS.getCode())
        );
        String localProject = null;
        for (Application application : applicationList) {
            Project local = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, application.getProjectId())
                            .eq(Project::getProjectIsLocal, IsLocalEnum.LOCAL.getCode())
            );
            if (local != null) {
                localProject = application.getProjectId();
                break;
            }
        }
        if (localProject == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "您还没有参加匠心项目");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, localProject)
        );
        ScanProjectResponse scanProjectResponse = new ScanProjectResponse();
        scanProjectResponse.setProjectId(project.getProjectId());
        scanProjectResponse.setProjectName(project.getProjectName());
        scanProjectResponse.setProjectOrigin(project.getProjectOrigin());
        scanProjectResponse.setProjectPeople(project.getProjectNeedNumber());
        scanProjectResponse.setAudit(project.getStage());
        String stage = null;
        List<Batch> batchList = batchMapper.selectList(
                Wrappers.lambdaQuery(Batch.class)
        );
        if (batchList.size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "暂无批次");
        }
        Batch batch = batchList.get(0);
        for (int i = 1; i < batchList.size(); i++) {
            if (batchList.get(i).getDefendDeadline().isAfter(batchList.get(i - 1).getDefendDeadline())) {
                batch = batchList.get(i);
            }
        }
        if (LocalDateTime.now().isAfter(batch.getEnrollBeginning()) && LocalDateTime.now().isBefore(batch.getEnrollDeadline())) {
            stage = ProjectStageEnum.SELECT.getCode();
        } else if (LocalDateTime.now().isAfter(batch.getMediumBeginning()) && LocalDateTime.now().isBefore(batch.getMediumDeadline())) {
            stage = ProjectStageEnum.MEDIUM.getCode();
        } else if (LocalDateTime.now().isAfter(batch.getDefendBeginning()) && LocalDateTime.now().isBefore(batch.getDefendDeadline())) {
            stage = ProjectStageEnum.PRESENTATION.getCode();
        } else if (LocalDateTime.now().isAfter(batch.getDefendDeadline())) {
            stage = ProjectStageEnum.ENDING.getCode();
        }
        scanProjectResponse.setStage(stage);
        User user;
        Major major;
        List<String> teachers = new ArrayList<>();
        List<String> majors = new ArrayList<>();
        List<ProjectUser> ProjectUsers = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, project.getProjectId())
                        .eq(ProjectUser::getRelationId, ProjectUserRelationEnum.TEACHER.getCode())
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
        List<ProjectMajor> ProjectMajors = ProjectMajorMapper.selectList(
                Wrappers.lambdaQuery(ProjectMajor.class)
                        .eq(ProjectMajor::getProjectId, project.getProjectId())

        );
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
        scanProjectResponse.setTeachers(teachers);
        scanProjectResponse.setMajors(majors);
        return scanProjectResponse;
    }
}
