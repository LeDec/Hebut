package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.*;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.mapper.*;
import com.Hebut.JiangXin.project.service.IProjectService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2021-12-07
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Resource
    ProjectMapper projectMapper;
    @Resource
    ProjectMajorMapper ProjectMajorMapper;
    @Resource
    ProjectUserMapper ProjectUserMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    MajorMapper majorMapper;
    @Resource
    BatchMapper batchMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    ExpenseMapper expenseMapper;
    @Resource
    ProjectScoreMapper projectScoreMapper;
    @Resource
    EvaluationMapper evaluationMapper;
    @Resource
    ProjectCertificateMapper ProjectCertificateMapper;
    @Resource
    ScoreMapper scoreMapper;
    @Resource
    InformMapper informMapper;

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
    public ShowProjectResponsePage showAllProject(ShowProjectRequest showProjectRequest) {
        if (showProjectRequest.getPageRequest().getCurrentPage() < 1 || showProjectRequest.getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, showProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, showProjectRequest.getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, showProjectRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "没有在进行的批次");
        }
        Page page = new Page(showProjectRequest.getPageRequest().getCurrentPage(), showProjectRequest.getPageRequest().getPageSize());
        List<ProjectUser> ProjectUsers;
        List<ProjectMajor> ProjectMajors;
        Page<Project> projectPage = projectMapper.selectPage(
                page,
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getBatchId, batch.getBatchId())
                        .eq(Project::getProjectIsLocal, showProjectRequest.getProjectType())
        );
        if (projectPage.getRecords().size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "项目不存在");
        }
        List<ShowProjectResponse> showProjectResponses = new ArrayList<>();
        for (Project project : projectPage.getRecords()) {
            User user;
            Major major;
            List<String> teachers = new ArrayList<>();
            List<String> majors = new ArrayList<>();
            ShowProjectResponse showProjectResponse = new ShowProjectResponse();
            showProjectResponse.setBatchName(batch.getBatchName());
            showProjectResponse.setProjectId(project.getProjectId());
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            showProjectResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            showProjectResponse.setProjectName(project.getProjectName().substring(index + 1));
            showProjectResponse.setProjectOrigin(project.getProjectOrigin());
            showProjectResponse.setProjectIntroduction(project.getProjectInstruction());
            String stage = project.getStage();
            if (batch.getEnrollBeginning().isBefore(LocalDateTime.now()) && batch.getEnrollDeadline().isAfter(LocalDateTime.now())) {
                stage = ProjectStageEnum.SELECT.getCode();
            } else if (batch.getMediumBeginning().isBefore(LocalDateTime.now()) && batch.getMediumDeadline().isAfter(LocalDateTime.now())) {
                stage = ProjectStageEnum.MEDIUM.getCode();
            } else if (batch.getDefendBeginning().isBefore(LocalDateTime.now()) && batch.getDefendDeadline().isAfter(LocalDateTime.now())) {
                stage = ProjectStageEnum.PRESENTATION.getCode();
            } else if (batch.getDefendDeadline().isBefore(LocalDateTime.now())) {
                stage = ProjectStageEnum.ENDING.getCode();
            }
            showProjectResponse.setStage(stage);
            showProjectResponse.setAudit(project.getStage());
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
            showProjectResponse.setUserId(projectUser.getUserId());
            showProjectResponse.setProjectTeacher(teachers);
            showProjectResponse.setProjectNeedMajor(majors);
            showProjectResponse.setProjectNeedPeople(project.getProjectNeedNumber());
            List<Expense> expenseList = expenseMapper.selectList(
                    Wrappers.lambdaQuery(Expense.class)
                            .eq(Expense::getProjectId, project.getProjectId())
                            .eq(Expense::getIsPass, ExpenseEnum.PASSING.getCode())
            );
            BigDecimal expenseAmount = new BigDecimal(0);
            for (Expense expense : expenseList) {
                expenseAmount = expenseAmount.add(expense.getAmount());
            }
            showProjectResponse.setAmount(expenseAmount);
            showProjectResponses.add(showProjectResponse);
        }
        for (ShowProjectResponse showProjectResponse : showProjectResponses) {
            List<ProjectUser> ProjectUserList = ProjectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getUserId, showProjectResponse.getUserId())
                            .eq(ProjectUser::getProjectId, showProjectResponse.getProjectId())
            );
            for (ProjectUser ProjectUser : ProjectUserList) {
                showProjectResponse.setRelationId(ProjectUser.getRelationId());
            }
        }
        Page<ShowProjectResponse> showProjectResponsePage = new Page<>(showProjectRequest.getPageRequest().getCurrentPage(), showProjectRequest.getPageRequest().getPageSize());
        showProjectResponsePage.setRecords(showProjectResponses);
        showProjectResponsePage.setTotal(showProjectResponses.size());
        ShowProjectResponsePage showProjectResponsePages = new ShowProjectResponsePage();
        showProjectResponsePages.setShowProjectResponsePage(showProjectResponsePage);
        showProjectResponsePages.setPages(pages(projectPage));
        return showProjectResponsePages;
    }

    @Override
    public void addProject(AddProjectRequest addProjectRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, addProjectRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Project project = new Project();
        String projectId = "project-" + UUID.randomUUID().toString().replace("\\-", "");
        project.setBatchId(addProjectRequest.getBatchId());
        project.setProjectId(projectId);
        project.setProjectName(addProjectRequest.getProjectName());
        project.setProjectOrigin(addProjectRequest.getProjectOrigin());
        project.setProjectNeedNumber(addProjectRequest.getProjectNeedPeople());
        project.setProjectInstruction(addProjectRequest.getProjectIntroduction());
        project.setProjectIsLocal(addProjectRequest.getProjectType());
        project.setStage(ProjectStageEnum.AUDIT.getCode());
        project.setRemainingAmount(addProjectRequest.getAmount());
        ProjectUser ProjectUser = new ProjectUser();
        for (int i = 0; i < addProjectRequest.getProjectTeacher().size(); i++) {
            ProjectUser.setProjectId(projectId);
            if (Integer.parseInt(addProjectRequest.getProjectType()) >= Integer.parseInt(IsLocalEnum.LOCAL.getCode())) {
                ProjectUser.setType(ProjectUserRelationEnum.TEACHER.getCode());
            } else if (addProjectRequest.getProjectType().equals(IsLocalEnum.OTHER.getCode())) {
                ProjectUser.setType(ProjectUserRelationEnum.APPLICANT.getCode());
            }
            ProjectUser.setUserId(addProjectRequest.getProjectTeacher().get(i));
            String relationIdPu = "PU-" + UUID.randomUUID().toString().replace("\\-", "");
            ProjectUser.setRelationId(relationIdPu);
            ProjectUserMapper.insert(ProjectUser);
        }
        ProjectMajor ProjectMajor = new ProjectMajor();
        for (int i = 0; i < addProjectRequest.getProjectNeedMajor().size(); i++) {
            ProjectMajor.setMajorId(addProjectRequest.getProjectNeedMajor().get(i));
            ProjectMajor.setProjectId(projectId);
            String relationIdPm = "PM-" + UUID.randomUUID().toString().replace("\\-", "");
            ProjectMajor.setRelationId(relationIdPm);
            ProjectMajorMapper.insert(ProjectMajor);
        }
        ProjectUser applicant = new ProjectUser();
        String applicantId = "PU-" + UUID.randomUUID().toString().replace("\\-", "");
        applicant.setRelationId(applicantId);
        applicant.setProjectId(projectId);
        applicant.setUserId(addProjectRequest.getCommonRequest().getUserId());
        applicant.setType(ProjectUserRelationEnum.APPLICANT.getCode());
        ProjectUserMapper.insert(applicant);
        projectMapper.insert(project);
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, addProjectRequest.getBatchId())
        );
        BatchProject batchProject = batchProjectMapper.selectOne(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, batch.getBatchId())
                        .eq(BatchProject::getProjectId, project.getProjectId())
        );
        if (batchProject != null) {
            throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "批次下已存在该项目");
        }
        BatchProject newBatchProject = new BatchProject();
        newBatchProject.setProjectId(project.getProjectId());
        newBatchProject.setBatchId(batch.getBatchId());
        String batchProjectId = "BP-" + UUID.randomUUID().toString().replace("\\-", "");
        newBatchProject.setRelationId(batchProjectId);
        batchProjectMapper.insert(newBatchProject);
    }

    @Override
    public PageUtil<ShowProjectResponse> searchProject(SearchProjectRequest searchProjectRequest) {
        if (searchProjectRequest.getPageRequest().getCurrentPage() < 1 || searchProjectRequest.getPageRequest().getPageSize() < 1) {
            throw new CustomException("请输入正确的页码");
        }
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, searchProjectRequest.getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, searchProjectRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "没有在进行的批次");
        }
        List<String> projectIdM = new ArrayList<>();
        List<String> projectIdU = new ArrayList<>();
        List<ProjectUser> projectUser;
        List<ShowProjectResponse> showProjectResponses = new ArrayList<>();
        List<Major> majors = majorMapper.selectList(
                Wrappers.lambdaQuery(Major.class)
                        .like(Major::getMajorName, searchProjectRequest.getProjectMajors())
        );
        List<Project> projects = projectMapper.selectList(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectIsLocal, searchProjectRequest.getProjectType())
        );
        List<User> users = userMapper.selectList(
                Wrappers.lambdaQuery(User.class)
                        .like(User::getUserName, searchProjectRequest.getProjectTeachers())
        );
        for (Project project : projects) {
            BatchProject batchProject = batchProjectMapper.selectOne(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getProjectId, project.getProjectId())
            );
            if (!batchProject.getBatchId().equals(batch.getBatchId())) {
                continue;
            }
            for (Major value : majors) {
                List<ProjectMajor> ProjectMajors = ProjectMajorMapper.selectList(
                        Wrappers.lambdaQuery(ProjectMajor.class)
                                .eq(ProjectMajor::getMajorId, value.getMajorId())
                                .eq(ProjectMajor::getProjectId, project.getProjectId())
                );
                for (ProjectMajor majorRelation : ProjectMajors) {
                    if (!projectIdM.contains(majorRelation.getProjectId())) {
                        projectIdM.add(majorRelation.getProjectId());
                    }
                }
            }
            for (User value : users) {
                projectUser = ProjectUserMapper.selectList(
                        Wrappers.lambdaQuery(ProjectUser.class)
                                .eq(ProjectUser::getUserId, value.getUserId())
                                .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
                                .eq(ProjectUser::getProjectId, project.getProjectId())
                );
                for (ProjectUser userRelation : projectUser) {
                    if (!projectIdU.contains(userRelation.getProjectId())) {
                        projectIdU.add(userRelation.getProjectId());
                    }
                }
            }
        }
        projectIdM.removeAll(projectIdU);
        projectIdU.addAll(projectIdM);
        for (String projectId : projectIdU) {
            User user;
            Major major;
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .like(Project::getProjectName, searchProjectRequest.getProjectSymbol())
                            .like(Project::getProjectName, searchProjectRequest.getProjectName())
                            .like(Project::getProjectOrigin, searchProjectRequest.getProjectOrigin())
                            .eq(Project::getProjectId, projectId)
            );
            if (project == null) {
                continue;
            }
            List<String> teachers = new ArrayList<>();
            List<String> majorList = new ArrayList<>();
            ShowProjectResponse showProjectResponse = new ShowProjectResponse();
            showProjectResponse.setBatchName(batch.getBatchName());
            showProjectResponse.setProjectId(project.getProjectId());
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
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            showProjectResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            showProjectResponse.setProjectName(project.getProjectName().substring(index + 1));
            showProjectResponse.setProjectOrigin(project.getProjectOrigin());
            showProjectResponse.setProjectIntroduction(project.getProjectInstruction());
            List<ProjectUser> ProjectUsers = ProjectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, project.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
            );
            List<ProjectMajor> ProjectMajors = ProjectMajorMapper.selectList(
                    Wrappers.lambdaQuery(ProjectMajor.class)
                            .eq(ProjectMajor::getProjectId, project.getProjectId())
            );
            for (ProjectUser ProjectUserA : ProjectUsers) {
                user = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, ProjectUserA.getUserId())
                );
                teachers.add(user.getUserName());
            }
            if (teachers.size() == 0) {
                teachers.add("暂无指导老师");
            }
            for (ProjectMajor ProjectMajorA : ProjectMajors) {
                major = majorMapper.selectOne(
                        Wrappers.lambdaQuery(Major.class)
                                .eq(Major::getMajorId, ProjectMajorA.getMajorId())
                );
                majorList.add(major.getMajorName());
            }
            if (majorList.size() == 0) {
                majorList.add("任意专业");
            }
            ProjectUser applicant = ProjectUserMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, project.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.APPLICANT.getCode())
            );
            List<Expense> expenseList = expenseMapper.selectList(
                    Wrappers.lambdaQuery(Expense.class)
                            .eq(Expense::getProjectId, project.getProjectId())
                            .eq(Expense::getIsPass, ExpenseEnum.PASSING.getCode())
            );
            BigDecimal expenseAmount = new BigDecimal(0);
            for (Expense expense : expenseList) {
                expenseAmount = expenseAmount.add(expense.getAmount());
            }
            showProjectResponse.setAmount(expenseAmount);
            showProjectResponse.setUserId(applicant.getUserId());
            showProjectResponse.setProjectNeedMajor(majorList);
            showProjectResponse.setProjectTeacher(teachers);
            showProjectResponse.setProjectNeedPeople(project.getProjectNeedNumber());
            showProjectResponses.add(showProjectResponse);
        }
        for (ShowProjectResponse showProjectResponse : showProjectResponses) {
            List<ProjectUser> ProjectUserList = ProjectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getUserId, searchProjectRequest.getCommonRequest().getUserId())
                            .eq(ProjectUser::getProjectId, showProjectResponse.getProjectId())
            );
            if (ProjectUserList.size() == 0) {
                showProjectResponse.setRelationId("游客");
            }
            for (ProjectUser ProjectUserObject : ProjectUserList) {
                if (Objects.equals(ProjectUserObject.getType(), ProjectUserRelationEnum.APPLICANT.getCode())) {
                    showProjectResponse.setRelationId(ProjectUserRelationEnum.APPLICANT.getMsg());
                } else if (Objects.equals(ProjectUserObject.getType(), ProjectUserRelationEnum.EXPERT.getCode())) {
                    showProjectResponse.setRelationId((ProjectUserRelationEnum.EXPERT.getMsg()));
                } else if (Objects.equals(ProjectUserObject.getType(), ProjectUserRelationEnum.TEACHER.getCode())) {
                    showProjectResponse.setRelationId((ProjectUserRelationEnum.TEACHER.getMsg()));
                } else if (Objects.equals(ProjectUserObject.getType(), ProjectUserRelationEnum.PARTNER.getCode())) {
                    showProjectResponse.setRelationId((ProjectUserRelationEnum.PARTNER.getMsg()));
                }
            }
        }
        PageUtil<ShowProjectResponse> showProjectResponsePage = new PageUtil<>(searchProjectRequest.getPageRequest().getCurrentPage(), searchProjectRequest.getPageRequest().getPageSize(), showProjectResponses);
        return showProjectResponsePage;
    }

    @Override
    public ScanProjectResponse scanProject(ScanProjectRequest scanProjectRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, scanProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, scanProjectRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, scanProjectRequest.getProjectId())
        );
        if (project == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "项目不存在");
        }
        List<String> teachers = new ArrayList<>();
        List<String> majors = new ArrayList<>();
        List<ProjectUser> ProjectUsers = ProjectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, project.getProjectId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
        );
        List<ProjectMajor> ProjectMajors = ProjectMajorMapper.selectList(
                Wrappers.lambdaQuery(ProjectMajor.class)
                        .eq(ProjectMajor::getProjectId, project.getProjectId())

        );
        for (ProjectUser ProjectUser : ProjectUsers) {
            User teacher = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, ProjectUser.getUserId())
            );
            if (teacher == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "指导老师不存在");
            }
            teachers.add(teacher.getUserName());
        }
        for (ProjectMajor ProjectMajor : ProjectMajors) {
            Major major = majorMapper.selectOne(
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
        scanProjectResponse.setStage(project.getStage());
        scanProjectResponse.setTeachers(teachers);
        scanProjectResponse.setMajors(majors);
        File localPath = new File("resource/prospectus");
        scanProjectResponse.setFilePath(localPath.getAbsolutePath() + "\\");
        scanProjectResponse.setFileName(project.getProjectInstruction());
        return scanProjectResponse;
    }

    @Override
    public DownloadResponse downloadProspectus(DownloadProspectusRequest downloadProspectusRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, downloadProspectusRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, downloadProspectusRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, downloadProspectusRequest.getProjectId())
        );
        DownloadResponse downloadResponse = new DownloadResponse();
        File localPath = new File(PathEnum.FilePath.getMsg() + "prospectus/");
        downloadResponse.setFilePath(localPath.getAbsolutePath());
        downloadResponse.setFileName(project.getProjectInstruction());
        return downloadResponse;
    }

    @Override
    public void auditProject(AuditProjectRequest auditProjectRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, auditProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, auditProjectRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, auditProjectRequest.getProjectId())
                        .eq(Project::getStage, ProjectStageEnum.AUDIT.getCode())
        );
        if (project == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到该项目");
        }
        int index = project.getProjectName().lastIndexOf('_');
        if (index == -1) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
        }
        String projectSymbol = project.getProjectName().substring(0, index);
        String projectName = project.getProjectName().substring(index + 1);
        String informId = "inform-" + UUID.randomUUID().toString().replace("\\-", "");
        Inform inform = new Inform();
        inform.setInformId(informId);
        ProjectUser applicant = ProjectUserMapper.selectOne(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, auditProjectRequest.getProjectId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.APPLICANT.getCode())
        );
        if (auditProjectRequest.isPass()) {
            project.setStage(ProjectStageEnum.WAIT.getCode());
            projectMapper.updateById(project);
            inform.setTitle("项目申报情况");
            inform.setSenderId(auditProjectRequest.getCommonRequest().getUserId());
            inform.setAcceptorId(applicant.getUserId());
            inform.setInformation("您的项目：" + projectSymbol + "-" + projectName + "项目初审已通过。");
            informMapper.insert(inform);
        } else {
            ProjectUserMapper.delete(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, project.getProjectId())
            );
            ProjectMajorMapper.delete(
                    Wrappers.lambdaQuery(ProjectMajor.class)
                            .eq(ProjectMajor::getProjectId, project.getProjectId())
            );
            projectScoreMapper.delete(
                    Wrappers.lambdaQuery(ProjectScore.class)
                            .eq(ProjectScore::getProjectId, project.getProjectId())
            );
            batchProjectMapper.delete(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getProjectId, project.getProjectId())
            );
            File file = new File("D:\\code\\JXProject\\java\\Prospectus\\" + project.getProjectInstruction());
            if (file.delete()) {
                projectMapper.deleteById(project);
            } else {
                throw new CustomException(ErrorEnum.FILE_DELETE_ERROR.getCode(), "文件删除失败");
            }
            inform.setTitle("项目申报情况");
            inform.setSenderId(auditProjectRequest.getCommonRequest().getUserId());
            inform.setAcceptorId(applicant.getUserId());
            inform.setInformation("您的项目：" + projectSymbol + "-" + projectName + "的项目初审未通过。");
            informMapper.insert(inform);
        }
    }

    @Override
    public Page<ShowAllOtherProjectResponse> showAllOtherProject(ShowAllOtherProjectRequest showAllOtherProjectRequest) {
        if (showAllOtherProjectRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || showAllOtherProjectRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, showAllOtherProjectRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, showAllOtherProjectRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Page<ShowAllOtherProjectResponse> showAllOtherProjectResponsePage = new Page<>();
        showAllOtherProjectResponsePage.setCurrent(showAllOtherProjectRequest.getCheckRequest().getPageRequest().getCurrentPage());
        showAllOtherProjectResponsePage.setSize(showAllOtherProjectRequest.getCheckRequest().getPageRequest().getPageSize());
        Page page = new Page(showAllOtherProjectRequest.getCheckRequest().getPageRequest().getCurrentPage(), showAllOtherProjectRequest.getCheckRequest().getPageRequest().getPageSize());
        List<ProjectUser> ProjectUsers;
        List<ProjectMajor> ProjectMajors;
        Page<Project> projectPage = null;
        if (Objects.equals(showAllOtherProjectRequest.getProjectStage(), ProjectStageEnum.SELECT.getCode())) {
            projectPage = projectMapper.selectPage(
                    page,
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getStage, showAllOtherProjectRequest.getProjectStage())
                            .eq(Project::getProjectIsLocal, IsLocalEnum.OTHER.getCode())
            );
        } else if (Objects.equals(showAllOtherProjectRequest.getProjectStage(), ProjectStageEnum.MEDIUM.getCode())) {
            projectPage = projectMapper.selectPage(
                    page,
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getStage, showAllOtherProjectRequest.getProjectStage())
                            .eq(Project::getProjectIsLocal, IsLocalEnum.OTHER.getCode())
            );
        } else if (Objects.equals(showAllOtherProjectRequest.getProjectStage(), ProjectStageEnum.PRESENTATION.getCode())) {
            projectPage = projectMapper.selectPage(
                    page,
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getStage, showAllOtherProjectRequest.getProjectStage())
                            .eq(Project::getProjectIsLocal, IsLocalEnum.OTHER.getCode())
            );
        } else if (Objects.equals(showAllOtherProjectRequest.getProjectStage(), ProjectStageEnum.ENDING.getCode())) {
            projectPage = projectMapper.selectPage(
                    page,
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getStage, showAllOtherProjectRequest.getProjectStage())
                            .eq(Project::getProjectIsLocal, IsLocalEnum.OTHER.getCode())
            );
        }
        List<ShowAllOtherProjectResponse> showAllOtherProjectResponses = new ArrayList<>();
        for (Project project : projectPage.getRecords()) {
            User user;
            Major major;
            List<String> teachers = new ArrayList<>();
            List<String> majors = new ArrayList<>();
            ShowAllOtherProjectResponse showAllOtherProjectResponse = new ShowAllOtherProjectResponse();
            showAllOtherProjectResponse.setProjectId(project.getProjectId());
            showAllOtherProjectResponse.setProjectName(project.getProjectName());
            showAllOtherProjectResponse.setProjectOrigin(project.getProjectOrigin());
            showAllOtherProjectResponse.setProjectIntroduction(project.getProjectInstruction());
            showAllOtherProjectResponse.setStage(project.getStage());
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
            showAllOtherProjectResponse.setUserId(projectUser.getUserId());
            showAllOtherProjectResponse.setProjectTeacher(teachers);
            showAllOtherProjectResponse.setProjectNeedMajor(majors);
            showAllOtherProjectResponse.setProjectNeedPeople(project.getProjectNeedNumber());
            showAllOtherProjectResponses.add(showAllOtherProjectResponse);
        }
        for (ShowAllOtherProjectResponse showAllOtherProjectResponse : showAllOtherProjectResponses) {
            List<ProjectUser> ProjectUserList = ProjectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getUserId, showAllOtherProjectRequest.getCheckRequest().getCommonRequest().getUserId())
                            .eq(ProjectUser::getProjectId, showAllOtherProjectResponse.getProjectId())
            );
            for (ProjectUser ProjectUser : ProjectUserList) {
                showAllOtherProjectResponse.setRelationId(ProjectUser.getRelationId());
            }
        }
        showAllOtherProjectResponsePage.setTotal(showAllOtherProjectResponses.size());
        showAllOtherProjectResponsePage.setRecords(showAllOtherProjectResponses);
        return showAllOtherProjectResponsePage;
    }

    @Override
    public void deleteProject(DeleteProjectRequest deleteProjectRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, deleteProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, deleteProjectRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, deleteProjectRequest.getProjectId())
        );
        if (project == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到该项目");
        } else {
            projectMapper.deleteById(project);
            ProjectUserMapper.delete(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, deleteProjectRequest.getProjectId())
            );
            ProjectMajorMapper.delete(
                    Wrappers.lambdaQuery(ProjectMajor.class)
                            .eq(ProjectMajor::getProjectId, deleteProjectRequest.getProjectId())
            );
            projectScoreMapper.delete(
                    Wrappers.lambdaQuery(ProjectScore.class)
                            .eq(ProjectScore::getProjectId, deleteProjectRequest.getProjectId())
            );
            ProjectCertificate projectCertificate = ProjectCertificateMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectCertificate.class)
                            .eq(ProjectCertificate::getProjectId, deleteProjectRequest.getProjectId())
            );
            File file = new File("D:\\code\\JXProject\\java\\img\\material\\" + projectCertificate.getCertificate());
            if (file.delete()) {
                ProjectCertificateMapper.deleteById(projectCertificate);
            } else {
                throw new CustomException(ErrorEnum.FILE_DELETE_ERROR.getCode(), "文件删除失败");
            }
            ProjectCertificateMapper.deleteById(projectCertificate);
            batchProjectMapper.delete(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getProjectId, deleteProjectRequest.getProjectId())
            );
            scoreMapper.delete(
                    Wrappers.lambdaQuery(Score.class)
                            .eq(Score::getProjectId, deleteProjectRequest.getProjectId())
            );
            evaluationMapper.delete(
                    Wrappers.lambdaQuery(Evaluation.class)
                            .eq(Evaluation::getProjectId, deleteProjectRequest.getProjectId())
            );
            expenseMapper.delete(
                    Wrappers.lambdaQuery(Expense.class)
                            .eq(Expense::getProjectId, deleteProjectRequest.getProjectId())
            );
        }
    }

    @Override
    public PageUtil<ShowPassProjectResponse> showPassProject(ShowPassProjectRequest showPassProjectRequest) {
        if (showPassProjectRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || showPassProjectRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, showPassProjectRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, showPassProjectRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, showPassProjectRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "没有在进行的批次");
        }
        List<ProjectUser> ProjectUsers;
        List<ProjectMajor> ProjectMajors;
        List<Project> projects = projectMapper.selectList(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getStage, ProjectStageEnum.WAIT.getCode())
        );
        if (projects.size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "没有未分配的项目");
        }
        List<ShowPassProjectResponse> showPassProjectResponseList = new ArrayList<>();
        for (Project project : projects) {
            User user;
            Major major;
            List<String> teachers = new ArrayList<>();
            List<String> majors = new ArrayList<>();
            BatchProject batchProject = batchProjectMapper.selectOne(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getProjectId, project.getProjectId())
                            .eq(BatchProject::getBatchId, showPassProjectRequest.getBatchId())
            );
            if (batchProject == null) {
                continue;
            }
            ShowPassProjectResponse showPassProjectResponse = new ShowPassProjectResponse();
            showPassProjectResponse.setProjectId(project.getProjectId());
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            showPassProjectResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            showPassProjectResponse.setProjectName(project.getProjectName().substring(index + 1));
            showPassProjectResponse.setProjectOrigin(project.getProjectOrigin());
            showPassProjectResponse.setProjectIntroduction(project.getProjectInstruction());
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
            showPassProjectResponse.setProjectTeacher(teachers);
            List<Expense> expenseList = expenseMapper.selectList(
                    Wrappers.lambdaQuery(Expense.class)
                            .eq(Expense::getProjectId, project.getProjectId())
                            .eq(Expense::getIsPass, ExpenseEnum.PASSING.getCode())
            );
            BigDecimal expenseAmount = new BigDecimal(0);
            for (Expense expense : expenseList) {
                expenseAmount = expenseAmount.add(expense.getAmount());
            }
            showPassProjectResponseList.add(showPassProjectResponse);
        }
        for (ShowPassProjectResponse showPassProjectResponse : showPassProjectResponseList) {
            List<ProjectUser> ProjectUserList = ProjectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.EXPERT.getCode())
                            .eq(ProjectUser::getProjectId, showPassProjectResponse.getProjectId())
            );
            if (ProjectUserList.size() == 0) {
                showPassProjectResponse.setStage("未分配");
            } else {
                showPassProjectResponse.setStage("已分配给：");
                for (ProjectUser ProjectUser : ProjectUserList) {
                    showPassProjectResponse.setStage(showPassProjectResponse.getStage() + ProjectUser.getUserId());
                    if (ProjectUserList.size() > 1) {
                        showPassProjectResponse.setStage(showPassProjectResponse.getStage() + ", ");
                    }
                }
            }
        }
        PageUtil<ShowPassProjectResponse> showPassProjectResponsePage = new PageUtil<>(showPassProjectRequest.getCheckRequest().getPageRequest().getCurrentPage(), showPassProjectRequest.getCheckRequest().getPageRequest().getPageSize(), showPassProjectResponseList);
        return showPassProjectResponsePage;
    }

    @Override
    public void addProjectDesignation(AddProjectDesignationRequest addProjectDesignationRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addProjectDesignationRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, addProjectDesignationRequest.getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, addProjectDesignationRequest.getProjectId())
        );
        int index = project.getProjectName().lastIndexOf('_');
        if (index == -1) {
            project.setProjectName(addProjectDesignationRequest.getDesignation() + "_" + project.getProjectName());
        } else {
            String projectSymbol = addProjectDesignationRequest.getDesignation();
            String projectName = project.getProjectName().substring(index + 1);
            project.setProjectName(projectSymbol + projectName);
        }
        projectMapper.updateById(project);
    }

    @Override
    public PageUtil<ShowAuditProjectResponse> showAuditProject(ShowAuditProjectRequest showAuditProjectRequest) {
        if (showAuditProjectRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || showAuditProjectRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, showAuditProjectRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, showAuditProjectRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户不存在或者在异地登录");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, showAuditProjectRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "没有在进行的批次");
        }
        List<ProjectUser> ProjectUsers;
        List<ProjectMajor> ProjectMajors;
        List<Project> projects = projectMapper.selectList(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectIsLocal, showAuditProjectRequest.getProjectType())
                        .eq(Project::getStage, ProjectStageEnum.AUDIT.getCode())
        );
        if (projects.size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到待审核的项目");
        }
        List<ShowAuditProjectResponse> showAuditProjectResponses = new ArrayList<>();
        for (Project project : projects) {
            User user;
            Major major;
            List<String> teachers = new ArrayList<>();
            List<String> majors = new ArrayList<>();
            BatchProject batchProject = batchProjectMapper.selectOne(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getProjectId, project.getProjectId())
                            .eq(BatchProject::getBatchId, showAuditProjectRequest.getBatchId())
            );
            if (batchProject == null) {
                continue;
            }
            ShowAuditProjectResponse showAuditProjectResponse = new ShowAuditProjectResponse();
            showAuditProjectResponse.setBatchName(batch.getBatchName());
            showAuditProjectResponse.setProjectId(project.getProjectId());
            showAuditProjectResponse.setProjectName(project.getProjectName());
            showAuditProjectResponse.setProjectOrigin(project.getProjectOrigin());
            showAuditProjectResponse.setProjectIntroduction(project.getProjectInstruction());
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
            showAuditProjectResponse.setUserId(projectUser.getUserId());
            showAuditProjectResponse.setProjectTeacher(teachers);
            showAuditProjectResponse.setProjectNeedMajor(majors);
            showAuditProjectResponse.setProjectNeedPeople(project.getProjectNeedNumber());
            showAuditProjectResponse.setAmount(project.getRemainingAmount());
            showAuditProjectResponses.add(showAuditProjectResponse);
        }
        PageUtil<ShowAuditProjectResponse> showAuditProjectResponsePage = new PageUtil<>(showAuditProjectRequest.getCheckRequest().getPageRequest().getCurrentPage(), showAuditProjectRequest.getCheckRequest().getPageRequest().getPageSize(), showAuditProjectResponses);
        return showAuditProjectResponsePage;
    }

    @Override
    public void processProject(ProcessProjectRequest processProjectRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, processProjectRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, processProjectRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.TEACHER.getCode())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "指导教师身份验证失败");
        }
        ProjectUser projectUser = ProjectUserMapper.selectOne(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, processProjectRequest.getProjectId())
                        .eq(ProjectUser::getUserId, token.getUserId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
        );
        if (projectUser == null) {
            throw new CustomException(ErrorEnum.MISMATCH_ERROR.getCode(), "您不是该项目的指导教师");
        }
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, processProjectRequest.getProjectId())
        );
        project.setStage(processProjectRequest.getStageId());
        projectMapper.updateById(project);
    }

    @Override
    public DownloadResponse downloadTemplate(DownloadTemplateRequest downloadTemplateRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, downloadTemplateRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, downloadTemplateRequest.getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "身份验证失败");
        }
        DownloadResponse downloadResponse = new DownloadResponse();
        File localPath = new File(PathEnum.FilePath.getMsg() + "template/project");
        File[] array = localPath.listFiles();
        downloadResponse.setFilePath(localPath.getAbsolutePath());
        downloadResponse.setFileName(array[0].getName());
        return downloadResponse;
    }

}
