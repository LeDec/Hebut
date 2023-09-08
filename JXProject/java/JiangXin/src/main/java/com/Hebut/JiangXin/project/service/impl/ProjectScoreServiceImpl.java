package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.*;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckPastProjectScoreResponse;
import com.Hebut.JiangXin.project.entity.response.CheckProjectAverageResponse;
import com.Hebut.JiangXin.project.entity.response.CheckProjectScoreResponse;
import com.Hebut.JiangXin.project.mapper.*;
import com.Hebut.JiangXin.project.service.IProjectScoreService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 项目打分表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-03-07
 */
@Service
public class ProjectScoreServiceImpl extends ServiceImpl<ProjectScoreMapper, ProjectScore> implements IProjectScoreService {

    @Resource
    ProjectScoreMapper projectScoreMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ProjectUserMapper projectUserMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    BatchMapper batchMapper;

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
    public void giveProjectScore(GiveProjectScoreRequest giveProjectScoreRequest) {
        if (giveProjectScoreRequest.getProjectId().size() != giveProjectScoreRequest.getScore().size()) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "本页还有未评分的项目");
        }
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, giveProjectScoreRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, giveProjectScoreRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.EXPERT.getCode())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "专家验证失败");
        }
        for (int i = 0; i < giveProjectScoreRequest.getProjectId().size(); i++) {
            ProjectUser projectUser = projectUserMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getUserId, token.getUserId())
                            .eq(ProjectUser::getProjectId, giveProjectScoreRequest.getProjectId().get(i))
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.EXPERT.getCode())
            );
            if (projectUser == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目未分配当前用户审核！");
            }
            ProjectScore projectScore = projectScoreMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectScore.class)
                            .eq(ProjectScore::getProjectId, giveProjectScoreRequest.getProjectId().get(i))
                            .eq(ProjectScore::getExpertId, giveProjectScoreRequest.getCommonRequest().getUserId())
            );
            if (projectScore == null) {
                ProjectScore score = new ProjectScore();
                score.setExpertId(token.getUserId());
                score.setProjectId(giveProjectScoreRequest.getProjectId().get(i));
                score.setScore(giveProjectScoreRequest.getScore().get(i));
                score.setSaveStage(SaveEnum.SAVE.getCode());
                projectScoreMapper.insert(score);
            } else if (Objects.equals(projectScore.getSaveStage(), SaveEnum.SAVE.getCode())) {
                projectScore.setScore(giveProjectScoreRequest.getScore().get(i));
                projectScoreMapper.updateById(projectScore);
            } else if (Objects.equals(projectScore.getSaveStage(), SaveEnum.SUBMIT.getCode())) {
                throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "您已提交改成绩，如需更改请联系管理员");
            }
        }
    }

    @Override
    public PageUtil<CheckProjectScoreResponse> checkProjectScore(CheckProjectScoreRequest checkProjectScoreRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkProjectScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkProjectScoreRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.EXPERT.getCode())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "专家验证失败");
        }
        List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getUserId, token.getUserId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.EXPERT.getCode())
        );
        List<CheckProjectScoreResponse> checkProjectScoreResponseList = new ArrayList<>();
        for (ProjectUser projectUser : ProjectUserList) {
            BatchProject batchProject = batchProjectMapper.selectOne(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getBatchId, checkProjectScoreRequest.getBatchId())
                            .eq(BatchProject::getProjectId, projectUser.getProjectId())
            );
            if (batchProject == null) {
                continue;
            }
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, projectUser.getProjectId())
            );
            CheckProjectScoreResponse checkProjectScoreResponse = new CheckProjectScoreResponse();
            ProjectScore projectScore = projectScoreMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectScore.class)
                            .eq(ProjectScore::getProjectId, projectUser.getProjectId())
                            .eq(ProjectScore::getExpertId, checkProjectScoreRequest.getCheckRequest().getCommonRequest().getUserId())
            );
            if (projectScore == null) {
                checkProjectScoreResponse.setScore(null);
            } else {
                checkProjectScoreResponse.setScore(projectScore.getScore().toString());
            }
            List<ProjectUser> teacherList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, projectUser.getProjectId())
                            .eq(ProjectUser::getRelationId, ProjectUserRelationEnum.TEACHER.getCode())
            );
            List<String> teachers = new ArrayList<>();
            for (ProjectUser teacher : teacherList) {
                User user = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, teacher.getUserId())
                );
                teachers.add(user.getUserName());
            }
            checkProjectScoreResponse.setTeachers(teachers);
            checkProjectScoreResponse.setOrigin(project.getProjectOrigin());
            checkProjectScoreResponse.setProjectId(project.getProjectId());
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            checkProjectScoreResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkProjectScoreResponse.setProjectName(project.getProjectName().substring(index + 1));
            checkProjectScoreResponse.setInstruction(project.getProjectInstruction());
            checkProjectScoreResponseList.add(checkProjectScoreResponse);
        }
        PageUtil<CheckProjectScoreResponse> checkProjectScoreResponsePage = new PageUtil<>(checkProjectScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkProjectScoreRequest.getCheckRequest().getPageRequest().getPageSize(), checkProjectScoreResponseList);
        return checkProjectScoreResponsePage;
    }

    @Override
    public PageUtil<CheckProjectAverageResponse> checkProjectAverage(CheckProjectAverageRequest checkProjectAverageRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkProjectAverageRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkProjectAverageRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, checkProjectAverageRequest.getBatchId())
        );
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, checkProjectAverageRequest.getBatchId())
        );
        List<CheckProjectAverageResponse> checkProjectAverageResponseList = new ArrayList<>();
        for (BatchProject batchProject : batchProjectList) {
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, batchProject.getProjectId())
            );
            CheckProjectAverageResponse checkProjectScoreResponse = new CheckProjectAverageResponse();
            List<ProjectUser> teacherList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, batchProject.getProjectId())
                            .eq(ProjectUser::getRelationId, ProjectUserRelationEnum.TEACHER.getCode())
            );
            List<String> teachers = new ArrayList<>();
            for (ProjectUser teacher : teacherList) {
                User user = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, teacher.getUserId())
                );
                teachers.add(user.getUserName());
            }
            List<ProjectScore> projectScores = projectScoreMapper.selectList(
                    Wrappers.lambdaQuery(ProjectScore.class)
                            .eq(ProjectScore::getProjectId, batchProject.getProjectId())
            );
            float sum = 0;

            for (ProjectScore score : projectScores) {
                sum += score.getScore();
            }
            if (projectScores.size() == 0) {
                checkProjectScoreResponse.setScore(-1);
            } else {
                checkProjectScoreResponse.setScore(sum / projectScores.size());
            }
            checkProjectScoreResponse.setTeachers(teachers);
            checkProjectScoreResponse.setOrigin(project.getProjectOrigin());
            checkProjectScoreResponse.setProjectId(project.getProjectId());
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            checkProjectScoreResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkProjectScoreResponse.setProjectName(project.getProjectName().substring(index + 1));
            checkProjectScoreResponse.setInstruction(project.getProjectInstruction());
            checkProjectScoreResponse.setAudit(project.getStage());
            String stage = null;
            if (batch.getSelectBeginning().isBefore(LocalDateTime.now()) && batch.getSelectDeadline().isAfter(LocalDateTime.now())) {
                stage = project.getStage();
            } else if (batch.getEnrollBeginning().isBefore(LocalDateTime.now()) && batch.getEnrollDeadline().isAfter(LocalDateTime.now())) {
                stage = ProjectStageEnum.SELECT.getCode();
            } else if (batch.getMediumBeginning().isBefore(LocalDateTime.now()) && batch.getMediumDeadline().isAfter(LocalDateTime.now())) {
                stage = ProjectStageEnum.MEDIUM.getCode();
            } else if (batch.getDefendBeginning().isBefore(LocalDateTime.now()) && batch.getDefendDeadline().isAfter(LocalDateTime.now())) {
                stage = ProjectStageEnum.PRESENTATION.getCode();
            } else if (batch.getDefendDeadline().isBefore(LocalDateTime.now())) {
                stage = ProjectStageEnum.ENDING.getCode();
            }
            checkProjectScoreResponse.setProjectStage(stage);
            checkProjectAverageResponseList.add(checkProjectScoreResponse);
        }
        Collections.sort(checkProjectAverageResponseList, CheckProjectAverageResponse.TotalScore);
        PageUtil<CheckProjectAverageResponse> checkProjectAverageResponsePage = new PageUtil<>(checkProjectAverageRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkProjectAverageRequest.getCheckRequest().getPageRequest().getPageSize(), checkProjectAverageResponseList);
        return checkProjectAverageResponsePage;
    }

    @Override
    public void submitProjectScore(SubmitProjectScoreRequest submitProjectScoreRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, submitProjectScoreRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, submitProjectScoreRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.EXPERT.getCode())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "专家验证失败");
        }
        for (int i = 0; i < submitProjectScoreRequest.getProjectId().size(); i++) {
            ProjectUser projectUser = projectUserMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getUserId, token.getUserId())
                            .eq(ProjectUser::getProjectId, submitProjectScoreRequest.getProjectId().get(i))
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.EXPERT.getCode())
            );
            if (projectUser == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目未分配当前用户审查");
            }
            ProjectScore projectScore = projectScoreMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectScore.class)
                            .eq(ProjectScore::getProjectId, submitProjectScoreRequest.getProjectId().get(i))
                            .eq(ProjectScore::getExpertId, submitProjectScoreRequest.getCommonRequest().getUserId())
            );
            if (projectScore == null) {
                ProjectScore score = new ProjectScore();
                score.setExpertId(token.getUserId());
                score.setProjectId(submitProjectScoreRequest.getProjectId().get(i));
                score.setScore(submitProjectScoreRequest.getScore().get(i));
                score.setSaveStage(SaveEnum.SAVE.getCode());
                projectScoreMapper.insert(score);
            } else if (Objects.equals(projectScore.getSaveStage(), SaveEnum.SAVE.getCode())) {
                projectScore.setScore(submitProjectScoreRequest.getScore().get(i));
                projectScore.setSaveStage(SaveEnum.SUBMIT.getCode());
                projectScoreMapper.updateById(projectScore);
            } else if (Objects.equals(projectScore.getSaveStage(), SaveEnum.SUBMIT.getCode())) {
                throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "您已提交改成绩，如需更改请联系管理员");
            }
        }
    }

    @Override
    public PageUtil<CheckPastProjectScoreResponse> checkPastProjectScore(CheckPastProjectScoreRequest checkPastProjectScoreRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkPastProjectScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkPastProjectScoreRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.EXPERT.getCode())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "专家验证失败");
        }
        List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getUserId, token.getUserId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.EXPERT.getCode())
        );
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, checkPastProjectScoreRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "批次不存在");
        }
        List<CheckPastProjectScoreResponse> checkPastProjectScoreResponseList = new ArrayList<>();
        for (ProjectUser projectUser : ProjectUserList) {
            BatchProject batchProject = batchProjectMapper.selectOne(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getBatchId, batch.getBatchId())
                            .eq(BatchProject::getProjectId, projectUser.getProjectId())
            );
            if (batchProject == null) {
                continue;
            }
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, projectUser.getProjectId())
            );
            CheckPastProjectScoreResponse checkPastProjectScoreResponse = new CheckPastProjectScoreResponse();
            ProjectScore projectScore = projectScoreMapper.selectOne(
                    Wrappers.lambdaQuery(ProjectScore.class)
                            .eq(ProjectScore::getProjectId, projectUser.getProjectId())
                            .eq(ProjectScore::getExpertId, checkPastProjectScoreRequest.getCheckRequest().getCommonRequest().getUserId())
            );
            if (projectScore == null) {
                checkPastProjectScoreResponse.setScore(null);
            } else {
                checkPastProjectScoreResponse.setScore(projectScore.getScore().toString());
            }
            List<ProjectUser> teacherList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, projectUser.getProjectId())
                            .eq(ProjectUser::getRelationId, ProjectUserRelationEnum.TEACHER.getCode())
            );
            List<String> teachers = new ArrayList<>();
            for (ProjectUser teacher : teacherList) {
                User user = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, teacher.getUserId())
                );
                teachers.add(user.getUserName());
            }
            checkPastProjectScoreResponse.setBatchId(batch.getBatchId());
            checkPastProjectScoreResponse.setBatchName(batch.getBatchName());
            checkPastProjectScoreResponse.setTeachers(teachers);
            checkPastProjectScoreResponse.setOrigin(project.getProjectOrigin());
            checkPastProjectScoreResponse.setProjectId(project.getProjectId());
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            checkPastProjectScoreResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkPastProjectScoreResponse.setProjectName(project.getProjectName().substring(index + 1));
            checkPastProjectScoreResponse.setInstruction(project.getProjectInstruction());
            checkPastProjectScoreResponseList.add(checkPastProjectScoreResponse);
        }
        PageUtil<CheckPastProjectScoreResponse> checkPastProjectScoreResponsePage = new PageUtil<>(checkPastProjectScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkPastProjectScoreRequest.getCheckRequest().getPageRequest().getPageSize(), checkPastProjectScoreResponseList);
        return checkPastProjectScoreResponsePage;
    }
}
