package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.Enum.ProjectUserRelationEnum;
import com.Hebut.JiangXin.common.Enum.UserTypeEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.*;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckAdviceResponse;
import com.Hebut.JiangXin.project.entity.response.CheckOwnEvaluationResponse;
import com.Hebut.JiangXin.project.entity.response.CheckRankResponse;
import com.Hebut.JiangXin.project.entity.response.EvaluationStageResponse;
import com.Hebut.JiangXin.project.mapper.*;
import com.Hebut.JiangXin.project.service.IEvaluationService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 教评表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-04
 */
@Service
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements IEvaluationService {

    @Resource
    UserMapper userMapper;
    @Resource
    EvaluationMapper evaluationMapper;
    @Resource
    ProjectUserMapper ProjectUserMapper;
    @Resource
    BatchMapper batchMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    ActivityMapper activityMapper;


    @Override
    public void fillEvaluation(AddEvaluationRequest addEvaluationRequest) {
        User student = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addEvaluationRequest.getStudentId())
        );
        User teacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, addEvaluationRequest.getTeacherId())
        );
        if (student == null || teacher == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "用户不存在");
        }
        BatchProject batchProject = batchProjectMapper.selectOne(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getProjectId, addEvaluationRequest.getProjectId())
        );
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, batchProject.getBatchId())
        );
        Activity activity = activityMapper.selectOne(
                Wrappers.lambdaQuery(Activity.class)
                        .eq(Activity::getTitle, "教评活动")
                        .eq(Activity::getBatchId, batch.getBatchId())
        );
        if (activity == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "本批次还未开启教评");
        }
        if (activity.getBeginning().isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorEnum.OVERDUE_ERROR.getCode(), "未到教评时间");
        } else if (activity.getDeadline() != null && activity.getDeadline().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorEnum.OVERDUE_ERROR.getCode(), "已过教评时间");
        }
        int projectUser = ProjectUserMapper.selectCount(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, addEvaluationRequest.getProjectId())
                        .eq(ProjectUser::getUserId, addEvaluationRequest.getTeacherId())
                        .eq(ProjectUser::getType, ProjectUserRelationEnum.TEACHER.getCode())
        );
        if (projectUser == 0) {
            throw new CustomException(ErrorEnum.MISMATCH_ERROR.getCode(), "评教老师必须是您参加项目的老师");
        }

        Evaluation evaluation = new Evaluation();
        String evaluationId = "evaluation-" + UUID.randomUUID().toString().replace("\\-", "");
        evaluation.setEvaluationId(evaluationId);
        evaluation.setStudentId(addEvaluationRequest.getStudentId());
        evaluation.setTeacherId(addEvaluationRequest.getTeacherId());
        evaluation.setGrade1(addEvaluationRequest.getGradeOne());
        evaluation.setGrade2(addEvaluationRequest.getGradeTwo());
        evaluation.setGrade3(addEvaluationRequest.getGradeThree());
        evaluation.setGrade4(addEvaluationRequest.getGradeFour());
        evaluation.setSuggestion1(addEvaluationRequest.getSuggestionOne());
        evaluation.setSuggestion2(addEvaluationRequest.getSuggestionTwo());
        evaluation.setSuggestion3(addEvaluationRequest.getSuggestionThree());
        evaluation.setSuggestion4(addEvaluationRequest.getSuggestionFour());
        evaluation.setSuggestion(addEvaluationRequest.getSuggestion());
        evaluation.setProjectId(addEvaluationRequest.getProjectId());
        evaluationMapper.insert(evaluation);
    }

    @Override
    public PageUtil<CheckRankResponse> checkRank(CheckRankRequest checkRankRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkRankRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkRankRequest.getCheckRequest().getCommonRequest().getToken())
                        .between(User::getType, UserTypeEnum.ADMIN.getCode(), UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        if (checkRankRequest.getCheckRequest().getPageRequest().getPageSize() < 1 || checkRankRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        List<CheckRankResponse> checkRankResponseList = new ArrayList<>();
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, checkRankRequest.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "批次不存在");
        }
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, batch.getBatchId())
        );
        List<String> teacherList = new ArrayList<>();
        for (BatchProject batchProject : batchProjectList) {
            List<Evaluation> evaluationList = evaluationMapper.selectList(
                    Wrappers.lambdaQuery(Evaluation.class)
                            .eq(Evaluation::getProjectId, batchProject.getProjectId())
            );
            for (Evaluation evaluation : evaluationList) {
                if (!teacherList.contains(evaluation.getTeacherId())) {
                    teacherList.add(evaluation.getTeacherId());
                    CheckRankResponse checkRankResponse = new CheckRankResponse();
                    List<Evaluation> teacher = evaluationMapper.selectList(
                            Wrappers.lambdaQuery(Evaluation.class)
                                    .eq(Evaluation::getTeacherId, evaluation.getTeacherId())
                    );
                    checkRankResponse.setTeacherId(evaluation.getTeacherId());
                    User user = userMapper.selectOne(
                            Wrappers.lambdaQuery(User.class)
                                    .eq(User::getUserId, evaluation.getTeacherId())
                    );
                    checkRankResponse.setTeacherName(user.getUserName());
                    for (Evaluation piece : teacher) {
                        checkRankResponse.setScoreOne(piece.getGrade1() / teacher.size() + checkRankResponse.getScoreOne());
                        checkRankResponse.setScoreTwo(piece.getGrade2() / teacher.size() + checkRankResponse.getScoreTwo());
                        checkRankResponse.setScoreThree(piece.getGrade3() / teacher.size() + checkRankResponse.getScoreThree());
                        checkRankResponse.setScoreFour(piece.getGrade4() / teacher.size() + checkRankResponse.getScoreFour());
                    }
                    float total = checkRankResponse.getScoreOne() + checkRankResponse.getScoreTwo() + checkRankResponse.getScoreThree() + checkRankResponse.getScoreFour();
                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    String totalScore = decimalFormat.format(total);
                    checkRankResponse.setTotalScore(totalScore);
                    checkRankResponseList.add(checkRankResponse);
                } else {
                    continue;
                }
            }
        }
        Collections.sort(checkRankResponseList, CheckRankResponse.TotalScore);
        PageUtil<CheckRankResponse> checkRankResponsePage = new PageUtil<>(checkRankRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkRankRequest.getCheckRequest().getPageRequest().getPageSize(), checkRankResponseList);
        return checkRankResponsePage;
    }

    @Override
    public PageUtil<CheckAdviceResponse> checkAdvice(CheckAdviceRequest checkAdviceRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkAdviceRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkAdviceRequest.getCheckRequest().getCommonRequest().getToken())
                        .between(User::getType, UserTypeEnum.ADMIN.getCode(), UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<CheckAdviceResponse> checkAdviceResponseList = new ArrayList<>();
        List<Evaluation> evaluationList = evaluationMapper.selectList(
                Wrappers.lambdaQuery(Evaluation.class)
                        .eq(Evaluation::getTeacherId, checkAdviceRequest.getTeacherId())
        );
        for (Evaluation evaluation : evaluationList) {
            CheckAdviceResponse checkAdviceResponse = new CheckAdviceResponse();
            User teacher = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, evaluation.getTeacherId())
            );
            checkAdviceResponse.setTeacherName(teacher.getUserName());
            checkAdviceResponse.setScoreOne(evaluation.getGrade1());
            checkAdviceResponse.setAdviceOne(evaluation.getSuggestion1());
            checkAdviceResponse.setScoreTwo(evaluation.getGrade2());
            checkAdviceResponse.setAdviceTwo(evaluation.getSuggestion2());
            checkAdviceResponse.setScoreThree(evaluation.getGrade3());
            checkAdviceResponse.setAdviceThree(evaluation.getSuggestion3());
            checkAdviceResponse.setScoreFour(evaluation.getGrade4());
            checkAdviceResponse.setAdviceFour(evaluation.getSuggestion4());
            checkAdviceResponse.setScoreTotal(evaluation.getGrade1() + evaluation.getGrade2() + evaluation.getGrade3() + evaluation.getGrade4());
            checkAdviceResponse.setAdviceTotal(evaluation.getSuggestion());
            checkAdviceResponseList.add(checkAdviceResponse);
        }
        PageUtil<CheckAdviceResponse> checkAdviceResponsePage = new PageUtil<>(checkAdviceRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkAdviceRequest.getCheckRequest().getPageRequest().getPageSize(), checkAdviceResponseList);
        return checkAdviceResponsePage;
    }

    @Override
    public void publishEvaluation(PublishEvaluationRequest publishEvaluationRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, publishEvaluationRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, publishEvaluationRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Activity activity = activityMapper.selectOne(
                Wrappers.lambdaQuery(Activity.class)
                        .eq(Activity::getBatchId, publishEvaluationRequest.getBatchId())
                        .eq(Activity::getTitle, "教评活动")
        );
        String activityId = "activity-" + UUID.randomUUID().toString().replace("\\-", "");
        if (activity == null) {
            Batch batch = batchMapper.selectOne(
                    Wrappers.lambdaQuery(Batch.class)
                            .eq(Batch::getBatchId, publishEvaluationRequest.getBatchId())
            );
            if (batch == null) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "批次不存在");
            }
            Activity evaluation = new Activity();
            evaluation.setActivityId(activityId);
            evaluation.setTitle("教评活动");
            evaluation.setIntroduction("请各位同学对自己的指导老师进行教评。");
            evaluation.setBatchId(publishEvaluationRequest.getBatchId());
            evaluation.setBeginning(publishEvaluationRequest.getBeginning());
            evaluation.setDeadline(publishEvaluationRequest.getDeadline());
            activityMapper.insert(evaluation);
        } else {
            throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "无法重复发布教评活动！");
        }
    }

    @Override
    public PageUtil<EvaluationStageResponse> evaluationStage(EvaluationStageRequest evaluationStageRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, evaluationStageRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, evaluationStageRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        List<EvaluationStageResponse> evaluationStageResponseList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        List<Batch> batchList = batchMapper.selectList(
                Wrappers.lambdaQuery(Batch.class)
        );
        for (Batch batch : batchList) {
            String stage = null;
            EvaluationStageResponse evaluationStageResponse = new EvaluationStageResponse();
            evaluationStageResponse.setBatchId(batch.getBatchId());
            evaluationStageResponse.setBatchName(batch.getBatchName());
            evaluationStageResponse.setSelectTime(batch.getSelectBeginning());
            String batchStage;
            if (batch.getSelectBeginning().isAfter(LocalDateTime.now())) {
                batchStage = "匠心班未开启";
            } else if (batch.getSelectBeginning().isBefore(LocalDateTime.now()) && batch.getEnrollBeginning().isAfter(LocalDateTime.now())) {
                batchStage = "征集阶段";
            } else if (batch.getEnrollBeginning().isBefore(LocalDateTime.now()) && batch.getMediumBeginning().isAfter(LocalDateTime.now())) {
                batchStage = "报名阶段";
            } else if (batch.getMediumBeginning().isBefore(LocalDateTime.now()) && batch.getDefendBeginning().isAfter(LocalDateTime.now())) {
                batchStage = "项目进行中期阶段";
            } else if (batch.getDefendBeginning().isBefore(LocalDateTime.now()) && batch.getDefendDeadline().isAfter(LocalDateTime.now())) {
                batchStage = "项目进行答辩阶段";
            } else {
                batchStage = "结束阶段";
            }
            evaluationStageResponse.setBatchStage(batchStage);
            Activity activity = activityMapper.selectOne(
                    Wrappers.lambdaQuery(Activity.class)
                            .eq(Activity::getBatchId, batch.getBatchId())
                            .eq(Activity::getTitle, "教评活动")
            );
            if (activity == null) {
                stage = "未开启";
                evaluationStageResponse.setBeginning(null);
            } else if (activity.getDeadline() == null || activity.getDeadline().isAfter(LocalDateTime.now()) && activity.getBeginning().isBefore(LocalDateTime.now())) {
                stage = "进行中";
                evaluationStageResponse.setBeginning(dateTimeFormatter.format(activity.getBeginning()));
            } else if (activity.getDeadline().isBefore(LocalDateTime.now())) {
                stage = "已结束";
                evaluationStageResponse.setBeginning(dateTimeFormatter.format(activity.getBeginning()));
            }
            if (activity == null || activity.getDeadline() == null) {
                evaluationStageResponse.setDeadline(null);
            } else {
                evaluationStageResponse.setDeadline(dateTimeFormatter.format(activity.getDeadline()));
            }
            evaluationStageResponse.setStage(stage);
            evaluationStageResponseList.add(evaluationStageResponse);
        }
        evaluationStageResponseList.sort(EvaluationStageResponse.evaluationTime);
        PageUtil<EvaluationStageResponse> evaluationStageResponsePage = new PageUtil<>(evaluationStageRequest.getCheckRequest().getPageRequest().getCurrentPage(), evaluationStageRequest.getCheckRequest().getPageRequest().getPageSize(), evaluationStageResponseList);
        return evaluationStageResponsePage;

    }

    @Override
    public PageUtil<CheckOwnEvaluationResponse> checkOwnEvaluation(CheckOwnEvaluationRequest checkOwnEvaluationRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkOwnEvaluationRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkOwnEvaluationRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.TEACHER.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "指导老师验证失败");
        }
        List<Evaluation> evaluationList = evaluationMapper.selectList(
                Wrappers.lambdaQuery(Evaluation.class)
                        .eq(Evaluation::getTeacherId, checkOwnEvaluationRequest.getCheckRequest().getCommonRequest().getUserId())
        );
        List<CheckOwnEvaluationResponse> checkOwnEvaluationResponseList = new ArrayList<>();
        for (Evaluation evaluation : evaluationList) {
            BatchProject batchProject = batchProjectMapper.selectOne(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getBatchId, checkOwnEvaluationRequest.getBatchId())
                            .eq(BatchProject::getProjectId, evaluation.getProjectId())
            );
            if (batchProject == null) {
                continue;
            }
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, evaluation.getProjectId())
            );

            int index = project.getProjectName().lastIndexOf('_');
            if (index == -1) {
                throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目还没有编号");
            }
            CheckOwnEvaluationResponse checkOwnEvaluationResponse = new CheckOwnEvaluationResponse();
            User student = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, evaluation.getStudentId())
            );
            checkOwnEvaluationResponse.setProjectSymbol(project.getProjectName().substring(0, index));
            checkOwnEvaluationResponse.setProjectName(project.getProjectName().substring(index + 1));
            checkOwnEvaluationResponse.setStudentId(student.getUserId());
            checkOwnEvaluationResponse.setStudentName(student.getUserName());
            checkOwnEvaluationResponse.setScoreOne(evaluation.getGrade1());
            checkOwnEvaluationResponse.setAdviceOne(evaluation.getSuggestion1());
            checkOwnEvaluationResponse.setScoreTwo(evaluation.getGrade2());
            checkOwnEvaluationResponse.setAdviceTwo(evaluation.getSuggestion2());
            checkOwnEvaluationResponse.setScoreThree(evaluation.getGrade3());
            checkOwnEvaluationResponse.setAdviceThree(evaluation.getSuggestion3());
            checkOwnEvaluationResponse.setScoreFour(evaluation.getGrade4());
            checkOwnEvaluationResponse.setAdviceFour(evaluation.getSuggestion4());
            checkOwnEvaluationResponse.setScoreTotal(evaluation.getGrade1() + evaluation.getGrade2() + evaluation.getGrade3() + evaluation.getGrade4());
            checkOwnEvaluationResponse.setAdviceTotal(evaluation.getSuggestion());
            checkOwnEvaluationResponse.setUpdateTime(evaluation.getUpdateTime());
            checkOwnEvaluationResponseList.add(checkOwnEvaluationResponse);
        }
        checkOwnEvaluationResponseList.sort(CheckOwnEvaluationResponse.evaluationTime);
        PageUtil<CheckOwnEvaluationResponse> checkOwnEvaluationResponsePage = new PageUtil<>(checkOwnEvaluationRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkOwnEvaluationRequest.getCheckRequest().getPageRequest().getPageSize(), checkOwnEvaluationResponseList);
        return checkOwnEvaluationResponsePage;

    }

    @Override
    public void changeEvaluationTime(ChangeEvaluationTimeRequest changeEvaluationTimeRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, changeEvaluationTimeRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, changeEvaluationTimeRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Activity activity = activityMapper.selectOne(
                Wrappers.lambdaQuery(Activity.class)
                        .eq(Activity::getBatchId, changeEvaluationTimeRequest.getBatchId())
                        .eq(Activity::getTitle, "教评活动")
        );
        if (activity == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "还未发布教评活动");
        } else {
            activity.setBeginning(changeEvaluationTimeRequest.getBeginning());
            activity.setDeadline(changeEvaluationTimeRequest.getDeadline());
            activityMapper.updateById(activity);
        }
    }
}
