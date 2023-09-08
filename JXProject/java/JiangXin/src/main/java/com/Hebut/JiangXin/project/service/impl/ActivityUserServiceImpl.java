package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.*;
import com.Hebut.JiangXin.project.entity.request.ActivityActivityStatisticsRequest;
import com.Hebut.JiangXin.project.entity.request.CheckActivityStudentRequest;
import com.Hebut.JiangXin.project.entity.request.CheckSignOnRequest;
import com.Hebut.JiangXin.project.entity.request.SignOnActivityRequest;
import com.Hebut.JiangXin.project.entity.response.ActivitySignStatisticsResponse;
import com.Hebut.JiangXin.project.entity.response.CheckActivitySignResponse;
import com.Hebut.JiangXin.project.entity.response.CheckActivityStudentResponse;
import com.Hebut.JiangXin.project.mapper.*;
import com.Hebut.JiangXin.project.service.IActivityUserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 活动参与表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-02-06
 */
@Service
public class ActivityUserServiceImpl extends ServiceImpl<ActivityUserMapper, ActivityUser> implements IActivityUserService {

    @Resource
    UserMapper userMapper;
    @Resource
    ActivityUserMapper activityUserMapper;
    @Resource
    ActivityMapper activityMapper;
    @Resource
    BatchMapper batchMapper;
    @Resource
    InstituteMapper instituteMapper;
    @Resource
    MajorMapper majorMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    ProjectUserMapper ProjectUserMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    BatchUserMapper batchUserMapper;

    @Override
    public void signOnActivity(SignOnActivityRequest signOnActivityRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, signOnActivityRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, signOnActivityRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        for (String studentId : signOnActivityRequest.getStudentIdList()) {
            ActivityUser isSignOn = activityUserMapper.selectOne(
                    Wrappers.lambdaQuery(ActivityUser.class)
                            .eq(ActivityUser::getActivityId, signOnActivityRequest.getActivityId())
                            .eq(ActivityUser::getUserId, studentId)
                            .eq(ActivityUser::getStage, ActivityEnum.SIGN_ON.getCode())
            );
            if (isSignOn == null) {
                String relationId = "AU-" + UUID.randomUUID().toString().replace("\\-", "");
                ActivityUser activityUser = new ActivityUser();
                activityUser.setRelationId(relationId);
                activityUser.setActivityId(signOnActivityRequest.getActivityId());
                activityUser.setUserId(studentId);
                activityUser.setStage(ActivityEnum.SIGN_ON.getCode());
                activityUser.setStageTime(LocalDateTime.now());
                activityUserMapper.insert(activityUser);
            } else {
                throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), isSignOn.getUserId() + "学生已经签到");
            }
        }
    }

    @Override
    public Page<CheckActivitySignResponse> checkActivitySign(CheckSignOnRequest checkSignOnRequest) {
        if (checkSignOnRequest.getCheckRequest().getPageRequest().getCurrentPage() < 1 || checkSignOnRequest.getCheckRequest().getPageRequest().getPageSize() < 1) {
            throw new CustomException(ErrorEnum.PAGE_WRONG.getCode(), "请输入正确的页码");
        }
        User classTeacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkSignOnRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkSignOnRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (classTeacher == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Page page = new Page(checkSignOnRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkSignOnRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<CheckActivitySignResponse> checkActivitySignResponsePage = new Page<>(checkSignOnRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkSignOnRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<ActivityUser> activityUserPage = activityUserMapper.selectPage(
                page,
                Wrappers.lambdaQuery(ActivityUser.class)
                        .eq(ActivityUser::getActivityId, checkSignOnRequest.getActivityId())
        );
        List<CheckActivitySignResponse> checkActivitySignResponseList = new ArrayList<>();
        for (ActivityUser activityUser : activityUserPage.getRecords()) {
            String stage = null;
            User student = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, activityUser.getUserId())
            );
            CheckActivitySignResponse checkActivitySignResponse = new CheckActivitySignResponse();
            checkActivitySignResponse.setUserId(student.getUserId());
            checkActivitySignResponse.setUserName(student.getUserName());
            if (Objects.equals(activityUser.getStage(), ActivityEnum.ABSENCE.getCode())) {
                stage = ActivityEnum.ABSENCE.getMsg();
            } else if (Objects.equals(activityUser.getStage(), ActivityEnum.SIGN_ON.getCode())) {
                stage = ActivityEnum.SIGN_ON.getMsg();
            }
            checkActivitySignResponse.setStage(stage);
            checkActivitySignResponseList.add(checkActivitySignResponse);
        }
        checkActivitySignResponsePage.setRecords(checkActivitySignResponseList);
        BeanUtils.copyProperties(activityUserPage,checkActivitySignResponsePage);
        return checkActivitySignResponsePage;
    }


    @Override
    public Page<ActivitySignStatisticsResponse> activityActivityStatistics(ActivityActivityStatisticsRequest activityActivityStatisticsRequest) {
        User classTeacher = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, activityActivityStatisticsRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, activityActivityStatisticsRequest.getCheckRequest().getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (classTeacher == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Page page = new Page(activityActivityStatisticsRequest.getCheckRequest().getPageRequest().getCurrentPage(), activityActivityStatisticsRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<ActivitySignStatisticsResponse> activitySignStatisticsResponsePage = new Page<>(activityActivityStatisticsRequest.getCheckRequest().getPageRequest().getCurrentPage(), activityActivityStatisticsRequest.getCheckRequest().getPageRequest().getPageSize());
        List<ActivitySignStatisticsResponse> activitySignStatisticsResponseList = new ArrayList<>();
        Page<Activity> activityPage = activityMapper.selectPage(
                page,
                Wrappers.lambdaQuery(Activity.class)
                        .eq(Activity::getBatchId, activityActivityStatisticsRequest.getBatchId())
        );
        for (Activity activity : activityPage.getRecords()) {
            ActivitySignStatisticsResponse activitySignStatisticsResponse = new ActivitySignStatisticsResponse();
            activitySignStatisticsResponse.setActivityId(activity.getActivityId());
            activitySignStatisticsResponse.setActivityTitle(activity.getTitle());
            int participate = 0;
            List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                    Wrappers.lambdaQuery(BatchProject.class)
                            .eq(BatchProject::getBatchId, activityActivityStatisticsRequest.getBatchId())
            );
            for (BatchProject batchProject : batchProjectList) {
                List<ProjectUser> ProjectUserList = ProjectUserMapper.selectList(
                        Wrappers.lambdaQuery(ProjectUser.class)
                                .eq(ProjectUser::getProjectId, batchProject.getProjectId())
                                .eq(ProjectUser::getType, ProjectUserRelationEnum.PARTNER.getCode())
                );
                participate += ProjectUserList.size();
            }
            int signOn = activityUserMapper.selectCount(
                    Wrappers.lambdaQuery(ActivityUser.class)
                            .eq(ActivityUser::getActivityId, activity.getActivityId())
                            .eq(ActivityUser::getStage, ActivityEnum.SIGN_ON.getCode())
            );
            activitySignStatisticsResponse.setParticipate(participate);
            activitySignStatisticsResponse.setSignOn(signOn);
            activitySignStatisticsResponseList.add(activitySignStatisticsResponse);
        }
        activitySignStatisticsResponsePage.setRecords(activitySignStatisticsResponseList);
        BeanUtils.copyProperties(activityPage,activitySignStatisticsResponsePage);
        return activitySignStatisticsResponsePage;
    }

    @Override
    public Page<CheckActivityStudentResponse> checkActivityStudent(CheckActivityStudentRequest checkActivityStudentRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkActivityStudentRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkActivityStudentRequest.getCheckRequest().getCommonRequest().getToken())
                        .le(User::getType, UserTypeEnum.CLASS_TEACHER.getCode())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Activity activity = activityMapper.selectOne(
                Wrappers.lambdaQuery(Activity.class)
                        .eq(Activity::getActivityId, checkActivityStudentRequest.getActivityId())
        );
        if (activity == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "活动不存在");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, activity.getBatchId())
        );
        if (batch == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "批次不存在");
        }
        Page page = new Page(checkActivityStudentRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkActivityStudentRequest.getCheckRequest().getPageRequest().getPageSize());
        Page<CheckActivityStudentResponse> checkActivityStudentResponsePage = new Page<>(checkActivityStudentRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkActivityStudentRequest.getCheckRequest().getPageRequest().getPageSize());
        List<CheckActivityStudentResponse> checkActivityStudentResponses = new ArrayList<>();
        Page<BatchUser> batchUserPage = batchUserMapper.selectPage(
                page,
                Wrappers.lambdaQuery(BatchUser.class)
                        .eq(BatchUser::getBatchId, batch.getBatchId())
        );
        for (BatchUser batchUser : batchUserPage.getRecords()) {
            User information = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, batchUser.getUserId())
            );
            ActivityUser activityUser = activityUserMapper.selectOne(
                    Wrappers.lambdaQuery(ActivityUser.class)
                            .eq(ActivityUser::getActivityId, activity.getActivityId())
                            .eq(ActivityUser::getUserId, information.getUserId())
            );
            CheckActivityStudentResponse checkActivityStudentResponse = new CheckActivityStudentResponse();
            checkActivityStudentResponse.setUserId(information.getUserId());
            checkActivityStudentResponse.setUserName(information.getUserName());
            Institute institute = instituteMapper.selectOne(
                    Wrappers.lambdaQuery(Institute.class)
                            .eq(Institute::getInstituteId, information.getInstituteId())
            );
            checkActivityStudentResponse.setInstitute(institute.getInstituteName());
            Major major = majorMapper.selectOne(
                    Wrappers.lambdaQuery(Major.class)
                            .eq(Major::getMajorId, information.getMajorId())
            );
            checkActivityStudentResponse.setMajor(major.getMajorName());
            if (activityUser != null) {
                checkActivityStudentResponse.setSignId(activityUser.getStage());
                checkActivityStudentResponse.setSignStage(ActivityEnum.SIGN_ON.getMsg());
            } else {
                checkActivityStudentResponse.setSignId(ActivityEnum.ABSENCE.getCode());
                checkActivityStudentResponse.setSignStage(ActivityEnum.ABSENCE.getMsg());

            }
            checkActivityStudentResponses.add(checkActivityStudentResponse);
        }
        checkActivityStudentResponses.sort(CheckActivityStudentResponse.stage);
        BeanUtils.copyProperties(batchUserPage,checkActivityStudentResponsePage);
        checkActivityStudentResponsePage.setRecords(checkActivityStudentResponses);
        return checkActivityStudentResponsePage;
    }

}
