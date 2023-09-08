package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.*;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.Hebut.JiangXin.project.mapper.*;
import com.Hebut.JiangXin.project.service.IScoreService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-29
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements IScoreService {
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

    @Resource
    ScoreMapper scoreMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    ProjectUserMapper projectUserMapper;
    @Resource
    BatchMapper batchMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    ActivityMapper activityMapper;
    @Resource
    ActivityUserMapper activityUserMapper;

    @Override
    public SearchScoreResponse checkOwnScore(CheckOwnScoreRequest checkOwnScoreRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkOwnScoreRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkOwnScoreRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您的账号在异地登录！");
        }
        Score usualScore = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getUserId, checkOwnScoreRequest.getCommonRequest().getUserId())
                        .eq(Score::getProjectId, checkOwnScoreRequest.getProjectId())
                        .eq(Score::getScoreType, ScoreTypeEnum.USUAL.getCode())
        );
        Score activityScore = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getUserId, checkOwnScoreRequest.getCommonRequest().getUserId())
                        .eq(Score::getProjectId, checkOwnScoreRequest.getProjectId())
                        .eq(Score::getScoreType, ScoreTypeEnum.ACTIVITY.getCode())
        );
        Score defenseScore = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getUserId, checkOwnScoreRequest.getCommonRequest().getUserId())
                        .eq(Score::getProjectId, checkOwnScoreRequest.getProjectId())
                        .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
        );

        SearchScoreResponse searchScoreResponse = new SearchScoreResponse();
        if (activityScore == null) {
            searchScoreResponse.setActivityScore(-1);
        } else {
            searchScoreResponse.setActivityScore(activityScore.getScore());
        }
        if (defenseScore == null) {
            searchScoreResponse.setDefenseScore(-1);
        } else {
            searchScoreResponse.setDefenseScore(defenseScore.getScore());
        }
        if (usualScore == null) {
            searchScoreResponse.setUsualScore(-1);
        } else {
            searchScoreResponse.setUsualScore(usualScore.getScore());
        }
        if (activityScore == null || defenseScore == null || usualScore == null) {
            searchScoreResponse.setScore(-1);
        } else {
            searchScoreResponse.setScore(searchScoreResponse.getDefenseScore() + searchScoreResponse.getUsualScore() + searchScoreResponse.getActivityScore());
        }
        return searchScoreResponse;
    }

    @Override
    public Page<CheckStudentScoreResponse> checkStudentScore(SearchAllScoreRequest searchAllScoreRequest) {
        if (searchAllScoreRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || searchAllScoreRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, searchAllScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, searchAllScoreRequest.getCheckRequest().getCommonRequest().getToken())
                        .between(User::getType, UserTypeEnum.ADMIN.getCode(), UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "没有权限，或账号在异地登录");
        }
        List<Score> scores = scoreMapper.selectList(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getProjectId, searchAllScoreRequest.getProjectId())
        );
        List<String> studentId = new ArrayList<>();
        for (Score score : scores) {
            if (!studentId.contains(score.getUserId())) {
                studentId.add(score.getUserId());
            }
        }
        Page<CheckStudentScoreResponse> checkStudentScoreResponsePage = new Page<>(searchAllScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), searchAllScoreRequest.getCheckRequest().getPageRequest().getPageSize());
        List<CheckStudentScoreResponse> checkStudentScoreResponseList = new ArrayList<>();
        for (String student : studentId) {
            CheckStudentScoreResponse checkStudentScoreResponse = new CheckStudentScoreResponse();
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, searchAllScoreRequest.getProjectId())
            );
            checkStudentScoreResponse.setProjectId(project.getProjectId());
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            checkStudentScoreResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkStudentScoreResponse.setProjectName(project.getProjectName().substring(index + 1));
            checkStudentScoreResponse.setUserId(student);
            Score usualScore = scoreMapper.selectOne(
                    Wrappers.lambdaQuery(Score.class)
                            .eq(Score::getUserId, student)
                            .eq(Score::getProjectId, searchAllScoreRequest.getProjectId())
                            .eq(Score::getScoreType, ScoreTypeEnum.USUAL.getCode())
            );
            Score activityScore = scoreMapper.selectOne(
                    Wrappers.lambdaQuery(Score.class)
                            .eq(Score::getUserId, student)
                            .eq(Score::getProjectId, searchAllScoreRequest.getProjectId())
                            .eq(Score::getScoreType, ScoreTypeEnum.ACTIVITY.getCode())
            );
            Score defenseScore = scoreMapper.selectOne(
                    Wrappers.lambdaQuery(Score.class)
                            .eq(Score::getUserId, student)
                            .eq(Score::getProjectId, searchAllScoreRequest.getProjectId())
                            .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
            );
            if (activityScore == null) {
                checkStudentScoreResponse.setActivityScore(-1);
            } else {
                checkStudentScoreResponse.setActivityScore(activityScore.getScore());
            }
            if (defenseScore == null) {
                checkStudentScoreResponse.setDefenseScore(-1);
            } else {
                checkStudentScoreResponse.setDefenseScore(defenseScore.getScore());
            }
            if (usualScore == null) {
                checkStudentScoreResponse.setUsualScore(-1);
            } else {
                checkStudentScoreResponse.setUsualScore(usualScore.getScore());
            }
            if (activityScore == null || defenseScore == null || usualScore == null) {
                checkStudentScoreResponse.setScore(-1);
            } else {
                checkStudentScoreResponse.setScore(checkStudentScoreResponse.getDefenseScore() + checkStudentScoreResponse.getUsualScore() + checkStudentScoreResponse.getActivityScore());
            }
            User user = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, student)
                            .eq(User::getType, UserTypeEnum.STUDENT.getCode())
            );
            checkStudentScoreResponse.setUserName(user.getUserName());
            checkStudentScoreResponseList.add(checkStudentScoreResponse);
        }
        checkStudentScoreResponsePage.setRecords(checkStudentScoreResponseList);
        return checkStudentScoreResponsePage;
    }

    @Override
    public void modifyScore(ModifyScoreRequest modifyScoreRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, modifyScoreRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, modifyScoreRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证错误");
        }
        Score score = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getUserId, modifyScoreRequest.getStudentId())
                        .eq(Score::getScoreType, modifyScoreRequest.getScoreType())
        );
        if (score == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该学生还没有该项成绩");
        }
        Score adminScore = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getProjectId, "system")
                        .eq(Score::getScoreType, modifyScoreRequest.getScoreType())
        );
        if (adminScore == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "管理员未设置该项成绩占比");
        }
        score.setScore(modifyScoreRequest.getScore() * adminScore.getScore());
        scoreMapper.updateById(score);
    }

    @Override
    public void giveScore(GiveScoreRequest giveScoreRequest) {
        if (giveScoreRequest.getProjectId().size() != giveScoreRequest.getScore().size()) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "本页还有未评分的学生");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, giveScoreRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, giveScoreRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        String scoreType = null;
        if (Objects.equals(user.getType(), UserTypeEnum.CLASS_TEACHER.getCode())) {
            scoreType = ScoreTypeEnum.ACTIVITY.getCode();
        } else if (Objects.equals(user.getType(), UserTypeEnum.TEACHER.getCode())) {
            scoreType = ScoreTypeEnum.USUAL.getCode();
        } else if (Objects.equals(user.getType(), UserTypeEnum.EXPERT.getCode())) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "没有权限");
        }
        Score admin = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getProjectId, "system")
                        .eq(Score::getScoreType, scoreType)
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "管理员未设置该项成绩占比");
        }
        for (String projectId : giveScoreRequest.getProjectId()) {
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, projectId)
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            if (ProjectUserList.size() == 0) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目没有学生参加，无法评定成绩");
            } else {
                for (int i = 0; i < giveScoreRequest.getProjectId().size(); i++) {
                    Score score = scoreMapper.selectOne(
                            Wrappers.lambdaQuery(Score.class)
                                    .eq(Score::getProjectId, projectId)
                                    .eq(Score::getUserId, giveScoreRequest.getUserId().get(i))
                                    .eq(Score::getScoreType, scoreType)
                    );
                    if (score == null) {
                        Score student = new Score();
                        student.setUserId(giveScoreRequest.getUserId().get(i));
                        student.setProjectId(projectId);
                        student.setSaveStage(SaveEnum.SAVE.getCode());
                        student.setScoreType(scoreType);
                        if (giveScoreRequest.getScore().get(i) == null) {
                            float s = 0;
                            student.setScore(s);
                        } else {
                            student.setScore(giveScoreRequest.getScore().get(i) * admin.getScore());
                        }
                        scoreMapper.insert(student);
                    } else if (Objects.equals(score.getSaveStage(), SaveEnum.SAVE.getCode())) {
                        score.setScore(giveScoreRequest.getScore().get(i) * admin.getScore());
                        scoreMapper.updateById(score);
                    } else if (Objects.equals(score.getSaveStage(), SaveEnum.SUBMIT.getCode())) {
                        throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "您已提交改成绩，如需更改请联系管理员");
                    }
                }
            }
        }
    }

    @Override
    public void setStandard(SetStandardRequest setStandardRequest) {
        if (setStandardRequest.getDefense() + setStandardRequest.getActivity() + setStandardRequest.getUsual() != CommonEnum.SCORE_LIMIT.getCode()) {
            throw new CustomException(ErrorEnum.NUMBER_OVERFLOW.getCode(), "三项占比加起来不为100%");
        }
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, setStandardRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, setStandardRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        Score adminUsual = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getUserId, setStandardRequest.getCommonRequest().getUserId())
                        .eq(Score::getScoreType, ScoreTypeEnum.USUAL.getCode())
        );
        Score adminActivity = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getUserId, setStandardRequest.getCommonRequest().getUserId())
                        .eq(Score::getScoreType, ScoreTypeEnum.ACTIVITY.getCode())
        );
        Score adminDefense = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getUserId, setStandardRequest.getCommonRequest().getUserId())
                        .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
        );
        if (adminUsual == null) {
            Score usual = new Score();
            usual.setUserId(setStandardRequest.getCommonRequest().getUserId());
            usual.setProjectId("system");
            usual.setScore(setStandardRequest.getUsual() / 100);
            usual.setScoreType(ScoreTypeEnum.USUAL.getCode());
            scoreMapper.insert(usual);
        } else {
            adminUsual.setScore(setStandardRequest.getUsual() / 100);
            scoreMapper.updateById(adminUsual);
        }
        if (adminActivity == null) {
            Score activity = new Score();
            activity.setUserId(setStandardRequest.getCommonRequest().getUserId());
            activity.setProjectId("system");
            activity.setScore(setStandardRequest.getActivity() / 100);
            activity.setScoreType(ScoreTypeEnum.ACTIVITY.getCode());
            scoreMapper.insert(activity);
        } else {
            adminActivity.setScore(setStandardRequest.getActivity() / 100);
            scoreMapper.updateById(adminActivity);
        }
        if (adminDefense == null) {
            Score defense = new Score();
            defense.setUserId(setStandardRequest.getCommonRequest().getUserId());
            defense.setProjectId("system");
            defense.setScore(setStandardRequest.getDefense() / 100);
            defense.setScoreType(ScoreTypeEnum.DEFENSE.getCode());
            scoreMapper.insert(defense);
        } else {
            adminDefense.setScore(setStandardRequest.getDefense() / 100);
            scoreMapper.updateById(adminDefense);
        }

    }

    @Override
    public CheckStandardResponse checkStandard(CommonRequest commonRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, commonRequest.getUserId())
                        .eq(User::getToken, commonRequest.getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        List<Score> ratio = scoreMapper.selectList(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getUserId, admin.getUserId())
                        .eq(Score::getProjectId, "system")
        );
        CheckStandardResponse checkStandardResponse = new CheckStandardResponse();
        for (Score score : ratio) {
            if (Objects.equals(score.getScoreType(), ScoreTypeEnum.DEFENSE.getCode())) {
                checkStandardResponse.setDefense(String.format("%.2f", score.getScore() * 100));
            } else if (Objects.equals(score.getScoreType(), ScoreTypeEnum.USUAL.getCode())) {
                checkStandardResponse.setUsual(String.format("%.2f", score.getScore() * 100));
            } else if (Objects.equals(score.getScoreType(), ScoreTypeEnum.ACTIVITY.getCode())) {
                checkStandardResponse.setActivity(String.format("%.2f", score.getScore() * 100));
            }
        }
        return checkStandardResponse;
    }

    @Override
    public void modifyScorePromote(ModifyScoreRequestPromoteRequest modifyScoreRequestPromoteRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, modifyScoreRequestPromoteRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, modifyScoreRequestPromoteRequest.getCommonRequest().getToken())
                        .ge(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
                        .le(User::getType, UserTypeEnum.EXPERT.getCode())
                        .eq(User::getIsPromote, IsPromoteEnum.PROMOTE.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        String scoreType = null;
        if (Objects.equals(user.getType(), UserTypeEnum.CLASS_TEACHER.getCode())) {
            scoreType = ScoreTypeEnum.ACTIVITY.getCode();
        } else if (Objects.equals(user.getType(), UserTypeEnum.EXPERT.getCode())) {
            scoreType = ScoreTypeEnum.DEFENSE.getCode();
        } else if (Objects.equals(user.getType(), UserTypeEnum.TEACHER.getCode())) {
            scoreType = ScoreTypeEnum.ACTIVITY.getCode();
        }
        Score student = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getUserId, modifyScoreRequestPromoteRequest.getStudentId())
                        .eq(Score::getScoreType, scoreType)
        );
        if (student == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到该学生");
        }
        Score admin = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getProjectId, "system")
                        .eq(Score::getScoreType, scoreType)
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "管理员未设置成绩占比");
        }
        student.setScore(modifyScoreRequestPromoteRequest.getScore() * admin.getScore());
        scoreMapper.updateById(student);
    }

    @Override
    public PageUtil<SearchAllScoreResponse> checkAllScore(CheckAllScoreRequest checkAllScoreRequest) {
        if (checkAllScoreRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkAllScoreRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User classTeacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkAllScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkAllScoreRequest.getCheckRequest().getCommonRequest().getToken())
                        .le(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (classTeacher == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, checkAllScoreRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "批次不存在");
        }
        List<SearchAllScoreResponse> searchAllScoreResponseList = new ArrayList<>();
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, batch.getBatchId())
        );
        for (BatchProject batchProject : batchProjectList) {
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, batchProject.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            for (ProjectUser projectUser : ProjectUserList) {
                User student = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, projectUser.getUserId())
                );

                Project project = projectMapper.selectOne(
                        Wrappers.lambdaQuery(Project.class)
                                .eq(Project::getProjectId, projectUser.getProjectId())
                );
                SearchAllScoreResponse searchAllScoreResponse = new SearchAllScoreResponse();
                Score usualScore = scoreMapper.selectOne(
                        Wrappers.lambdaQuery(Score.class)
                                .eq(Score::getUserId, student.getUserId())
                                .eq(Score::getProjectId, project.getProjectId())
                                .eq(Score::getScoreType, ScoreTypeEnum.USUAL.getCode())
                );
                Score activityScore = scoreMapper.selectOne(
                        Wrappers.lambdaQuery(Score.class)
                                .eq(Score::getUserId, student.getUserId())
                                .eq(Score::getProjectId, project.getProjectId())
                                .eq(Score::getScoreType, ScoreTypeEnum.ACTIVITY.getCode())
                );
                Score defenseScore = scoreMapper.selectOne(
                        Wrappers.lambdaQuery(Score.class)
                                .eq(Score::getUserId, student.getUserId())
                                .eq(Score::getProjectId, project.getProjectId())
                                .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
                );
                if (activityScore == null) {
                    searchAllScoreResponse.setActivityScore(-1);
                } else {
                    searchAllScoreResponse.setActivityScore(activityScore.getScore());
                }
                if (defenseScore == null) {
                    searchAllScoreResponse.setDefenseScore(-1);
                } else {
                    searchAllScoreResponse.setDefenseScore(defenseScore.getScore());
                }
                if (usualScore == null) {
                    searchAllScoreResponse.setUsualScore(-1);
                } else {
                    searchAllScoreResponse.setUsualScore(usualScore.getScore());
                }
                if (activityScore == null || defenseScore == null || usualScore == null) {
                    searchAllScoreResponse.setScore(-1);
                } else {
                    searchAllScoreResponse.setScore(searchAllScoreResponse.getDefenseScore() + searchAllScoreResponse.getUsualScore() + searchAllScoreResponse.getActivityScore());
                }
                int index = project.getProjectName().lastIndexOf('_');
                if (index == -1) {
                    throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
                }
                searchAllScoreResponse.setProjectId(project.getProjectId());
                searchAllScoreResponse.setProjectSymbol(project.getProjectName().substring(0, index));
                searchAllScoreResponse.setProjectName(project.getProjectName().substring(index + 1));
                searchAllScoreResponse.setUserId(student.getUserId());
                searchAllScoreResponse.setUserName(student.getUserName());
                List<ActivityUser> activityUserList = activityUserMapper.selectList(
                        Wrappers.lambdaQuery(ActivityUser.class)
                                .eq(ActivityUser::getUserId, student.getUserId())
                );
                for (int i = 0; i < activityUserList.size(); i++) {
                    Activity activity = activityMapper.selectOne(
                            Wrappers.lambdaQuery(Activity.class)
                                    .eq(Activity::getBatchId, checkAllScoreRequest.getBatchId())
                                    .eq(Activity::getActivityId, activityUserList.get(i).getActivityId())
                    );
                    if (activity == null) {
                        activityUserList.remove(i);
                    }
                }
                searchAllScoreResponse.setTimes(activityUserList.size());
                searchAllScoreResponseList.add(searchAllScoreResponse);
            }
        }
        return new PageUtil<>(checkAllScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkAllScoreRequest.getCheckRequest().getPageRequest().getPageSize(), searchAllScoreResponseList);
    }

    @Override
    public ShowAllDefenseScoreResponsePage showAllDefenseScore(ShowAllDefenseScoreRequest showAllDefenseScoreRequest) {
        if (showAllDefenseScoreRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || showAllDefenseScoreRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User expert = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, showAllDefenseScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, showAllDefenseScoreRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (expert == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Page page = new Page(showAllDefenseScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), showAllDefenseScoreRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<ProjectUser> projectUserPage = projectUserMapper.selectPage(
                page,
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getBatchId, showAllDefenseScoreRequest.getBatchId())
        );
        Score admin = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getProjectId, "system")
                        .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
        );
        List<ShowAllDefenseScoreResponse> showAllDefenseScoreResponseList = new ArrayList<>();
        for (ProjectUser projectUser : projectUserPage.getRecords()) {
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId,projectUser.getProjectId())
            );
            List<Score> scoreList = scoreMapper.selectList(
                    Wrappers.lambdaQuery(Score.class)
                            .eq(Score::getProjectId, project.getProjectId())
                            .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
            );
            ShowAllDefenseScoreResponse showAllDefenseScoreResponse = new ShowAllDefenseScoreResponse();
            List<String> teachers = new ArrayList<>();
            List<ProjectUser> teacherList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, project.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
            );
            for (ProjectUser teacher : teacherList) {
                User user = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, teacher.getUserId())
                );
                teachers.add(user.getUserName());
            }
            showAllDefenseScoreResponse.setTeachers(teachers);
            showAllDefenseScoreResponse.setOrigin(project.getProjectOrigin());
            showAllDefenseScoreResponse.setProjectId(project.getProjectId());
            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            showAllDefenseScoreResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            showAllDefenseScoreResponse.setProjectName(project.getProjectName().substring(index + 1));
            if (scoreList.size() == 0) {
                showAllDefenseScoreResponse.setScore(null);
            } else {
                showAllDefenseScoreResponse.setScore(String.valueOf(scoreList.get(0).getScore() / admin.getScore()));
            }
            showAllDefenseScoreResponseList.add(showAllDefenseScoreResponse);
        }
        Page<ShowAllDefenseScoreResponse> showAllDefenseScoreResponsePages = new Page<>(showAllDefenseScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), showAllDefenseScoreRequest.getCheckRequest().getPageRequest().getPageSize());
        showAllDefenseScoreResponsePages.setRecords(showAllDefenseScoreResponseList);
        showAllDefenseScoreResponsePages.setTotal(showAllDefenseScoreResponseList.size());
        ShowAllDefenseScoreResponsePage showAllDefenseScoreResponsePage = new ShowAllDefenseScoreResponsePage();
        showAllDefenseScoreResponsePage.setShowAllDefenseScoreResponsePage(showAllDefenseScoreResponsePages);
        showAllDefenseScoreResponsePage.setPages(pages(showAllDefenseScoreResponsePages));
        return showAllDefenseScoreResponsePage;

    }


    @Override
    public PageUtil<CheckScoreResponse> checkScore(CheckScoreRequest checkActivityScoreRequest) {
        if (checkActivityScoreRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkActivityScoreRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkActivityScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkActivityScoreRequest.getCheckRequest().getCommonRequest().getToken())
                        .between(User::getType, UserTypeEnum.CLASS_TEACHER.getCode(), UserTypeEnum.EXPERT.getCode())
        );
        List<CheckScoreResponse> checkScoreResponseList = new ArrayList<>();
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户验证失败");
        }
        String scoreType = null;
        if (Objects.equals(user.getType(), UserTypeEnum.CLASS_TEACHER.getCode())) {
            scoreType = ScoreTypeEnum.ACTIVITY.getCode();
        } else if (Objects.equals(user.getType(), UserTypeEnum.TEACHER.getCode())) {
            scoreType = ScoreTypeEnum.USUAL.getCode();
        } else if (Objects.equals(user.getType(), UserTypeEnum.EXPERT.getCode())) {
            scoreType = ScoreTypeEnum.DEFENSE.getCode();
        }
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, checkActivityScoreRequest.getBatchId())
        );
        for (BatchProject batchProject : batchProjectList) {
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, batchProject.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            for (ProjectUser ProjectUser : ProjectUserList) {
                User student = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, ProjectUser.getUserId())
                );
                Score score = scoreMapper.selectOne(
                        Wrappers.lambdaQuery(Score.class)
                                .eq(Score::getUserId, ProjectUser.getUserId())
                                .eq(Score::getScoreType, scoreType)
                );
                Project project = projectMapper.selectOne(
                        Wrappers.lambdaQuery(Project.class)
                                .eq(Project::getProjectId, ProjectUser.getProjectId())
                );
                CheckScoreResponse checkScoreResponse = new CheckScoreResponse();
                checkScoreResponse.setUserId(student.getUserId());
                int index = project.getProjectName().lastIndexOf('_');
                if (index == -1) {
                    throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
                }
                checkScoreResponse.setProjectSymbol(project.getProjectName().substring(0, index));
                checkScoreResponse.setProjectName(project.getProjectName().substring(index + 1));
                checkScoreResponse.setProjectId(project.getProjectId());
                checkScoreResponse.setProjectName(project.getProjectName());
                if (score == null) {
                    checkScoreResponse.setScore(null);
                } else {
                    checkScoreResponse.setScore(score.getScore().toString());
                }
                checkScoreResponseList.add(checkScoreResponse);
            }
        }
        return new PageUtil<>(checkActivityScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkActivityScoreRequest.getCheckRequest().getPageRequest().getPageSize(), checkScoreResponseList);
    }

    @Override
    public PageUtil<CheckRemainScoreResponse> checkRemainScore(CheckScoreRequest checkScoreRequest) {
        if (checkScoreRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkScoreRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkScoreRequest.getCheckRequest().getCommonRequest().getToken())
                        .between(User::getType, UserTypeEnum.TEACHER.getCode(), UserTypeEnum.EXPERT.getCode())
        );
        List<CheckRemainScoreResponse> checkRemainScoreResponseList = new ArrayList<>();
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户验证失败");
        }
        String scoreType = null;
        if (Objects.equals(user.getType(), UserTypeEnum.TEACHER.getCode())) {
            scoreType = ScoreTypeEnum.USUAL.getCode();
        } else if (Objects.equals(user.getType(), UserTypeEnum.EXPERT.getCode())) {
            scoreType = ScoreTypeEnum.DEFENSE.getCode();
        }
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, checkScoreRequest.getBatchId())
        );
        for (BatchProject batchProject : batchProjectList) {
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, batchProject.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            for (ProjectUser ProjectUser : ProjectUserList) {
                User student = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, ProjectUser.getUserId())
                );
                Score score = scoreMapper.selectOne(
                        Wrappers.lambdaQuery(Score.class)
                                .eq(Score::getUserId, ProjectUser.getUserId())
                                .eq(Score::getScoreType, scoreType)
                );
                Project project = projectMapper.selectOne(
                        Wrappers.lambdaQuery(Project.class)
                                .eq(Project::getProjectId, ProjectUser.getProjectId())
                );
                CheckRemainScoreResponse checkRemainScoreResponse = new CheckRemainScoreResponse();
                if (score == null) {
                    checkRemainScoreResponse.setUserId(student.getUserId());
                    checkRemainScoreResponse.setUserName(student.getUserName());
                    checkRemainScoreResponse.setProjectId(project.getProjectId());
                    checkRemainScoreResponse.setProjectName(project.getProjectName());
                    checkRemainScoreResponseList.add(checkRemainScoreResponse);
                }
            }
        }
        return new PageUtil<>(checkScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkScoreRequest.getCheckRequest().getPageRequest().getPageSize(), checkRemainScoreResponseList);
    }

    @Override
    public void giveDefense(GiveDefenseRequest giveDefenseRequest) {
        if (giveDefenseRequest.getProjectId().size() != giveDefenseRequest.getScore().size()) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "本页还有未评分的项目");
        }
        User teacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, giveDefenseRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, giveDefenseRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.EXPERT.getCode())
                        .eq(User::getIsPromote, IsPromoteEnum.PROMOTE.getCode())
        );
        if (teacher == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "专家验证失败或没有该权限");
        }
        Score admin = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getProjectId, "system")
                        .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "管理员未设置成绩占比");
        }
        for (int i = 0; i < giveDefenseRequest.getProjectId().size(); i++) {
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, giveDefenseRequest.getProjectId().get(i))
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            if (ProjectUserList.size() == 0) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目没有学生参加，无法评定成绩");
            } else {
                for (ProjectUser ProjectUser : ProjectUserList) {
                    Score score = scoreMapper.selectOne(
                            Wrappers.lambdaQuery(Score.class)
                                    .eq(Score::getProjectId, giveDefenseRequest.getProjectId().get(i))
                                    .eq(Score::getUserId, ProjectUser.getUserId())
                                    .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
                    );
                    if (score == null) {
                        Score student = new Score();
                        student.setUserId(ProjectUser.getUserId());
                        student.setProjectId(giveDefenseRequest.getProjectId().get(i));
                        student.setSaveStage(SaveEnum.SAVE.getCode());
                        student.setScoreType(ScoreTypeEnum.DEFENSE.getCode());
                        student.setScore(giveDefenseRequest.getScore().get(i) * admin.getScore());
                        scoreMapper.insert(student);
                    } else if (Objects.equals(score.getSaveStage(), SaveEnum.SAVE.getCode())) {
                        score.setScore(giveDefenseRequest.getScore().get(i) * admin.getScore());
                        scoreMapper.updateById(score);
                    } else if (Objects.equals(score.getSaveStage(), SaveEnum.SUBMIT.getCode())) {
                        throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "您已提交改成绩，如需更改请联系管理员");
                    }
                }
            }
        }
        teacher.setIsPromote(IsPromoteEnum.NOPE.getCode());
        userMapper.updateById(teacher);
    }

    @Override
    public PageUtil<StudentSignStatisticsResponse> checkRemainActivityScore(CheckScoreRequest checkScoreRequest) {
        if (checkScoreRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkScoreRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkScoreRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        List<StudentSignStatisticsResponse> studentSignStatisticsResponseList = new ArrayList<>();
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户验证失败");
        }
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, checkScoreRequest.getBatchId())
        );
        for (BatchProject batchProject : batchProjectList) {
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, batchProject.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            for (ProjectUser ProjectUser : ProjectUserList) {
                User student = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, ProjectUser.getUserId())
                );
                Score score = scoreMapper.selectOne(
                        Wrappers.lambdaQuery(Score.class)
                                .eq(Score::getUserId, ProjectUser.getUserId())
                                .eq(Score::getScoreType, ScoreTypeEnum.ACTIVITY.getCode())
                );
                Project project = projectMapper.selectOne(
                        Wrappers.lambdaQuery(Project.class)
                                .eq(Project::getProjectId, ProjectUser.getProjectId())
                );
                StudentSignStatisticsResponse studentSignStatisticsResponse = new StudentSignStatisticsResponse();
                if (score == null) {
                    studentSignStatisticsResponse.setUserId(student.getUserId());
                    studentSignStatisticsResponse.setUserName(student.getUserName());
                    studentSignStatisticsResponse.setProjectId(project.getProjectId());
                    int index = project.getProjectName().lastIndexOf('_');
                    if (index == -1) {
                        throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
                    }
                    studentSignStatisticsResponse.setProjectSymbol(project.getProjectName().substring(0, index));
                    studentSignStatisticsResponse.setProjectName(project.getProjectName().substring(index + 1));
                    List<ActivityUser> activityUserList = activityUserMapper.selectList(
                            Wrappers.lambdaQuery(ActivityUser.class)
                                    .eq(ActivityUser::getUserId, student.getUserId())
                    );
                    for (int i = 0; i < activityUserList.size(); i++) {
                        Activity activity = activityMapper.selectOne(
                                Wrappers.lambdaQuery(Activity.class)
                                        .eq(Activity::getBatchId, checkScoreRequest.getBatchId())
                                        .eq(Activity::getActivityId, activityUserList.get(i).getActivityId())
                        );
                        if (activity == null) {
                            activityUserList.remove(i);
                        }
                    }
                    studentSignStatisticsResponse.setTimes(activityUserList.size());
                    studentSignStatisticsResponseList.add(studentSignStatisticsResponse);
                }
            }
        }
        return new PageUtil<>(checkScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkScoreRequest.getCheckRequest().getPageRequest().getPageSize(), studentSignStatisticsResponseList);
    }

    @Override
    public void submitScore(SubmitScoreRequest submitScoreRequest) {
        if (submitScoreRequest.getProjectId().size() != submitScoreRequest.getScore().size()) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "本页还有未评分的学生");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, submitScoreRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, submitScoreRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        String scoreType;
        if (Objects.equals(user.getType(), UserTypeEnum.CLASS_TEACHER.getCode())) {
            scoreType = ScoreTypeEnum.ACTIVITY.getCode();
        } else if (Objects.equals(user.getType(), UserTypeEnum.TEACHER.getCode())) {
            scoreType = ScoreTypeEnum.USUAL.getCode();
        } else {
            throw new CustomException(ErrorEnum.INVALID_ACCESS.getCode(), "无权限");
        }
        Score admin = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getProjectId, "system")
                        .eq(Score::getScoreType, scoreType)
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "管理员未设置该项成绩占比");
        }
        for (int i = 0; i < submitScoreRequest.getProjectId().size(); i++) {
            Score score = scoreMapper.selectOne(
                    Wrappers.lambdaQuery(Score.class)
                            .eq(Score::getProjectId, submitScoreRequest.getProjectId().get(i))
                            .eq(Score::getUserId, submitScoreRequest.getUserId().get(i))
                            .eq(Score::getScoreType, scoreType)
            );
            if (score == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "未找到对应学生");
            }
            if (Objects.equals(score.getSaveStage(), SaveEnum.SUBMIT.getCode())) {
                throw new CustomException(ErrorEnum.OVERDUE_ERROR.getCode(), "该成绩已经提交，如想更改清联系管理员");
            }
            score.setSaveStage(SaveEnum.SUBMIT.getCode());
            score.setScore(submitScoreRequest.getScore().get(i) * admin.getScore());
            scoreMapper.updateById(score);
        }
    }

    @Override
    public void submitDefense(SubmitDefenseRequest submitDefenseRequest) {
        if (submitDefenseRequest.getProjectId().size() != submitDefenseRequest.getScore().size()) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "本页还有未评分的项目");
        }
        User teacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, submitDefenseRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, submitDefenseRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.EXPERT.getCode())
        );
        if (teacher == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "专家验证失败");
        }
        if (Objects.equals(teacher.getIsPromote(), IsPromoteEnum.NOPE.getCode())) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您已提交过成绩，无权限修改");
        }
        Score admin = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getProjectId, "system")
                        .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "管理员未设置成绩占比");
        }
        for (int i = 0; i < submitDefenseRequest.getProjectId().size(); i++) {
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, submitDefenseRequest.getProjectId().get(i))
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            if (ProjectUserList.size() == 0) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目没有学生参加，无法评定成绩");
            } else {
                for (ProjectUser ProjectUser : ProjectUserList) {
                    Score score = scoreMapper.selectOne(
                            Wrappers.lambdaQuery(Score.class)
                                    .eq(Score::getProjectId, submitDefenseRequest.getProjectId().get(i))
                                    .eq(Score::getUserId, ProjectUser.getUserId())
                                    .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
                    );
                    if (score == null) {
                        Score student = new Score();
                        student.setUserId(ProjectUser.getUserId());
                        student.setProjectId(submitDefenseRequest.getProjectId().get(i));
                        student.setSaveStage(SaveEnum.SUBMIT.getCode());
                        student.setScoreType(ScoreTypeEnum.DEFENSE.getCode());
                        student.setScore(submitDefenseRequest.getScore().get(i) * admin.getScore());
                        scoreMapper.insert(student);
                    } else if (Objects.equals(score.getSaveStage(), SaveEnum.SAVE.getCode())) {
                        score.setSaveStage(SaveEnum.SUBMIT.getCode());
                        score.setScore(submitDefenseRequest.getScore().get(i) * admin.getScore());
                        scoreMapper.updateById(score);
                    } else if (Objects.equals(score.getSaveStage(), SaveEnum.SUBMIT.getCode())) {
                        throw new CustomException(ErrorEnum.OVERDUE_ERROR.getCode(), "您已提交成绩，不可重复提交");
                    }
                }
            }
        }
        teacher.setIsPromote(IsPromoteEnum.NOPE.getCode());
        userMapper.updateById(teacher);
    }

    @Override
    public PageUtil<SearchAllScoreResponse> checkGuideScore(CheckGuideScoreRequest checkGuideScoreRequest) {
        if (checkGuideScoreRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkGuideScoreRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User classTeacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkGuideScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkGuideScoreRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (classTeacher == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<SearchAllScoreResponse> searchAllScoreResponseList = new ArrayList<>();
        List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getUserId, checkGuideScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
        );
        for (ProjectUser projectUser : ProjectUserList) {
            List<ProjectUser> studentList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, projectUser.getProjectId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
            );
            for(ProjectUser partner : studentList)
            {
                User student = userMapper.selectOne(
                        Wrappers.lambdaQuery(User.class)
                                .eq(User::getUserId, partner.getUserId())
                );
                Project project = projectMapper.selectOne(
                        Wrappers.lambdaQuery(Project.class)
                                .eq(Project::getProjectId, partner.getProjectId())
                );
                SearchAllScoreResponse searchAllScoreResponse = new SearchAllScoreResponse();
                Score usualScore = scoreMapper.selectOne(
                        Wrappers.lambdaQuery(Score.class)
                                .eq(Score::getUserId, partner.getUserId())
                                .eq(Score::getProjectId, project.getProjectId())
                                .eq(Score::getScoreType, ScoreTypeEnum.USUAL.getCode())
                );
                Score activityScore = scoreMapper.selectOne(
                        Wrappers.lambdaQuery(Score.class)
                                .eq(Score::getUserId, partner.getUserId())
                                .eq(Score::getProjectId, project.getProjectId())
                                .eq(Score::getScoreType, ScoreTypeEnum.ACTIVITY.getCode())
                );
                Score defenseScore = scoreMapper.selectOne(
                        Wrappers.lambdaQuery(Score.class)
                                .eq(Score::getUserId, partner.getUserId())
                                .eq(Score::getProjectId, project.getProjectId())
                                .eq(Score::getScoreType, ScoreTypeEnum.DEFENSE.getCode())
                );
                if (activityScore == null) {
                    searchAllScoreResponse.setActivityScore(-1);
                } else {
                    searchAllScoreResponse.setActivityScore(activityScore.getScore());
                }
                if (defenseScore == null) {
                    searchAllScoreResponse.setDefenseScore(-1);
                } else {
                    searchAllScoreResponse.setDefenseScore(defenseScore.getScore());
                }
                if (usualScore == null) {
                    searchAllScoreResponse.setUsualScore(-1);
                } else {
                    searchAllScoreResponse.setUsualScore(usualScore.getScore());
                }
                if (activityScore == null || defenseScore == null || usualScore == null) {
                    searchAllScoreResponse.setScore(-1);
                } else {
                    searchAllScoreResponse.setScore(searchAllScoreResponse.getDefenseScore() + searchAllScoreResponse.getUsualScore() + searchAllScoreResponse.getActivityScore());
                }
                searchAllScoreResponse.setUserId(student.getUserId());
                searchAllScoreResponse.setUserName(student.getUserName());
                searchAllScoreResponse.setProjectId(project.getProjectId());
                int index = project.getProjectName().lastIndexOf('_');
                if (index == -1) {
                    throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
                }
                searchAllScoreResponse.setProjectSymbol(project.getProjectName().substring(0, index));
                searchAllScoreResponse.setProjectName(project.getProjectName().substring(index + 1));
                searchAllScoreResponseList.add(searchAllScoreResponse);
            }
        }
        return new PageUtil<>(checkGuideScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkGuideScoreRequest.getCheckRequest().getPageRequest().getPageSize(), searchAllScoreResponseList);
    }

    @Override
    public PageUtil<CheckScoreResponse> checkMyGuideUsualScore(CheckScoreRequest checkScoreRequest) {
        if (checkScoreRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkScoreRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkScoreRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkScoreRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.TEACHER.getCode())
        );
        Score admin = scoreMapper.selectOne(
                Wrappers.lambdaQuery(Score.class)
                        .eq(Score::getProjectId, "system")
                        .eq(Score::getScoreType, ScoreTypeEnum.USUAL.getCode())
        );
        List<CheckScoreResponse> checkScoreResponseList = new ArrayList<>();
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "用户验证失败");
        }
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, checkScoreRequest.getBatchId())
        );
        for (BatchProject batchProject : batchProjectList) {
            List<ProjectUser> ProjectUserList = projectUserMapper.selectList(
                    Wrappers.lambdaQuery(ProjectUser.class)
                            .eq(ProjectUser::getProjectId, batchProject.getProjectId())
                            .eq(ProjectUser::getUserId, user.getUserId())
                            .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
            );
            for (ProjectUser projectUser : ProjectUserList) {
                List<ProjectUser> studentList = projectUserMapper.selectList(
                        Wrappers.lambdaQuery(ProjectUser.class)
                                .eq(ProjectUser::getProjectId, projectUser.getProjectId())
                                .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
                );
                for (ProjectUser partner : studentList) {
                    User student = userMapper.selectOne(
                            Wrappers.lambdaQuery(User.class)
                                    .eq(User::getUserId, partner.getUserId())
                    );
                    Score score = scoreMapper.selectOne(
                            Wrappers.lambdaQuery(Score.class)
                                    .eq(Score::getUserId, partner.getUserId())
                                    .eq(Score::getScoreType, ScoreTypeEnum.USUAL.getCode())
                    );
                    Project project = projectMapper.selectOne(
                            Wrappers.lambdaQuery(Project.class)
                                    .eq(Project::getProjectId, partner.getProjectId())
                    );
                    CheckScoreResponse checkScoreResponse = new CheckScoreResponse();
                    checkScoreResponse.setUserId(student.getUserId());
                    int index = project.getProjectName().lastIndexOf('_');
                    if (index == -1) {
                        throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
                    }
                    checkScoreResponse.setProjectId(project.getProjectId());
                    checkScoreResponse.setProjectSymbol(project.getProjectName().substring(0, index));
                    checkScoreResponse.setProjectName(project.getProjectName().substring(index + 1));
                    checkScoreResponse.setUserName(student.getUserName());
                    if (score == null) {
                        checkScoreResponse.setScore(null);
                    } else {
                        checkScoreResponse.setScore(String.valueOf(score.getScore() / admin.getScore()));
                    }
                    checkScoreResponseList.add(checkScoreResponse);
                }
            }
        }
        return new PageUtil<>(checkScoreRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkScoreRequest.getCheckRequest().getPageRequest().getPageSize(), checkScoreResponseList);
    }

}
